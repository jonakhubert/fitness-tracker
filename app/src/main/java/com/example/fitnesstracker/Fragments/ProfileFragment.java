package com.example.fitnesstracker.Fragments;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.fitnesstracker.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class ProfileFragment extends Fragment {
    private View view;
    private TextView userName, userEmail;
    private String userId;
    private ImageView userImage;

    String imagePath;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;

    private DocumentReference docRef;
    private StorageReference storeRef;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        userId = firebaseAuth.getCurrentUser().getUid();
        imagePath = "images/" + userId + "_profile.jpg";

        docRef = firebaseFirestore.collection("users").document(userId);
        storeRef = FirebaseStorage.getInstance().getReference();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profile, container, false);

        userName = view.findViewById(R.id.userName);
        userEmail = view.findViewById(R.id.userEmail);
        userImage = view.findViewById(R.id.userImage);

        docRef.addSnapshotListener(getActivity(), (value, error) -> {
            userName.setText(value.getString("name"));
            userEmail.setText(value.getString("email"));
        });

        StorageReference profileRef = storeRef.child(imagePath);
        profileRef.getDownloadUrl().addOnSuccessListener
        (uri -> Picasso.get().load(uri).into(userImage));

        userImage.setOnClickListener(unused ->
        {
            // open gallery - ACTION_PICK - meaning we are going to receive data
            Intent openGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startForResult.launch(openGallery);
        });

        return view;
    }

    private void uploadImageToFirebaseStorage(Uri imageUri, String imagePath)
    {
        StorageReference fileRef = storeRef.child(imagePath);
        fileRef.putFile(imageUri);
    }

    // activity result callback after startForResult
    private final ActivityResultLauncher<Intent> startForResult = registerForActivityResult
    (
        new ActivityResultContracts.StartActivityForResult(), result ->
        {
            if(result != null && result.getResultCode() == RESULT_OK)
            {
                Intent openGallery = result.getData();
                if(openGallery != null)
                {
                    Uri imageUri = openGallery.getData();
                    Picasso.get().load(imageUri).into(userImage);
                    uploadImageToFirebaseStorage(imageUri, imagePath);
                }
            }
        }
    );
}