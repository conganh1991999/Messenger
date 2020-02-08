package com.camm.booking;

public class LearningFirebase {

//          mData = FirebaseDatabase.getInstance().getReference();

//        // Set value data: String
//        mData.child("string").setValue("Anh Nguyen");
//
//        // Set value data: Object
//        user = new User("anh.nguyen", "123456", 19);
//        mData.child("user1").setValue(user);
//
//        // Set value data: Map
//        Map<String, Object> mMap = new HashMap<>();
//        mMap.put("user2", user);
//        mMap.put("user3", user);
//        mData.child("user f2").setValue(mMap);

//        // listen for set value completion
//        user = new User("anh.nguyen", "123456", 19);
//        mData.child("users").push().setValue(user, new DatabaseReference.CompletionListener() {
//            @Override
//            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
//                if(databaseError == null){
//                    Toast.makeText(MainActivity.this, "completed", Toast.LENGTH_SHORT).show();
//                }
//                else {
//                    Toast.makeText(MainActivity.this, "failed", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });


//        // get and listen to data
//        mData.child("Course").setValue("Data Mining");
//        mData.child("Course").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                if(dataSnapshot.getValue() != null)
//                    Toast.makeText(MainActivity.this, dataSnapshot.getValue().toString(), Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
}
