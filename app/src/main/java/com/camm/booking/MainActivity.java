package com.camm.booking;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    private CircleImageView profile;
    private TextView txtHaveAnAccount;
    private EditText edtRegisterEmail, edtRegisterPassword, edtRegisterUsername;
    private Button btnRegister;

    private int REQUEST_CODE_FOLDER = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Mapping();

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCompat.requestPermissions(
                        MainActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_CODE_FOLDER
                );
            }
        });

        txtHaveAnAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentToLogin = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intentToLogin);
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String username = edtRegisterUsername.getText().toString();
                final String email = edtRegisterEmail.getText().toString();
                final String password = edtRegisterPassword.getText().toString();

                if(email.equals("") || password.equals("") || username.equals("")){
                    Toast.makeText(MainActivity.this, "Fields can't be empty", Toast.LENGTH_SHORT).show();
                    validateAccount(username);
                }
                else{
                    final FirebaseAuth mAuth = FirebaseAuth.getInstance();
                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // If sign up successfully, save user to firebase dbs and go to login.
                                        Toast.makeText(MainActivity.this, "Sign up successfully", Toast.LENGTH_SHORT).show();
                                        saveUserToFirebase(mAuth.getCurrentUser(), username);
                                        gotoLoginActivity(email, password);
                                    } else {
                                        // If sign up fails, display a message to the user.
                                        if(task.getException() != null)
                                            Toast.makeText(MainActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });

    }

    private void Mapping(){
        profile = findViewById(R.id.profile_image);
        txtHaveAnAccount = findViewById(R.id.txtHaveAnAccount);
        edtRegisterEmail = findViewById(R.id.edtRegisterEmail);
        edtRegisterPassword = findViewById(R.id.edtRegisterPassword);
        edtRegisterUsername = findViewById(R.id.edtRegisterUsername);
        btnRegister = findViewById(R.id.btnRegister);
    }

    private void validateAccount(String username){
        String usernameRegex = "\\w*";
        String alert1 = "Username can only contain letters, digits and underscores";
        String alert2 = "Username too long";

        if(!Pattern.matches(usernameRegex, username)){
            Toast.makeText(MainActivity.this, alert1, Toast.LENGTH_SHORT).show();
            //edtRegisterUsername.setTextColor(Color.RED);
        }
        else if(username.length() > 50){
            Toast.makeText(MainActivity.this, alert2, Toast.LENGTH_SHORT).show();
        }
    }

    private void gotoLoginActivity(String carryEmail, String carryPassword){
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        intent.putExtra("email", carryEmail);
        intent.putExtra("password", carryPassword);
        startActivity(intent);
    }

    private void saveUserToFirebase(final FirebaseUser newUser, final String userName){

        final Calendar calendar = Calendar.getInstance();

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference("images");
        final StorageReference imageRef = storageRef.child("image" + calendar.getTimeInMillis() + ".png");

        // Get the data from an ImageView as bytes
        profile.setDrawingCacheEnabled(true);
        profile.buildDrawingCache();
        Bitmap bitmap = ((BitmapDrawable) profile.getDrawable()).getBitmap();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        byte[] data = outputStream.toByteArray();

        // Create a storage reference from our app
        UploadTask uploadTask = imageRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(MainActivity.this, exception.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        //TODO: create new function to save data
                        String userImage = uri.toString();
                        String userId;

                        if(newUser != null){
                            userId = newUser.getUid();
                        }
                        else {
                            userId = "error" + calendar.getTimeInMillis();
                            Log.d("error", "mAuth.getCurrentUser() error!");
                        }

                        User user = new User(userName, userImage);

                        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
                        DatabaseReference mRef = mDatabase.getReference("users");
                        mRef.child(userId).setValue(user);
                    }
                });
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(requestCode == REQUEST_CODE_FOLDER && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, REQUEST_CODE_FOLDER);
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == REQUEST_CODE_FOLDER && resultCode == RESULT_OK && data != null){
            Uri uri = data.getData();
            try {
                assert uri != null;
                InputStream inputStream = getContentResolver().openInputStream(uri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                profile.setImageBitmap(bitmap);
                // save image to sqlite
                // ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                // bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                // byte[] image = byteArrayOutputStream.toByteArray();
                // database.insertImage(image);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    //    private void autoLoadImage(){
//        database = new SQLiteHandler(this, "profile.sqlite", null, 1);
//        SQLiteDatabase db = database.getReadableDatabase();
//
//        Cursor cursorImage = db.query("Image", null, null, null, null, null, null);
//
//        if(cursorImage.moveToFirst()){
//            byte[] image = cursorImage.getBlob(1);
//            Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
//            profile.setImageBitmap(bitmap);
//        }
//        cursorImage.close();
//        db.close();
//    }
}
