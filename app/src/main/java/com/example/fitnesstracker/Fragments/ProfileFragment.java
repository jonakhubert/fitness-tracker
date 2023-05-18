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

import java.util.Objects;

/**
 * Fragment class for displaying the user's profile.
 */
public class ProfileFragment extends Fragment
{
    /**
     * The root view of the fragment.
     */
    private View view;
    /**
     * TextView for displaying the user's name and email.
     */
    private TextView userName, userEmail;
    /**
     * The ID of the user.
     */
    private String userId;
    /**
     * ImageView for displaying the user's image.
     */
    private ImageView userImage;
    /**
     * The path to the user's image.
     */
    private String imagePath;
    /**
     * Instance of FirebaseAuth for authentication.
     */
    private FirebaseAuth firebaseAuth;
    /**
     * Instance of FirebaseFirestore for database operations.
     */
    private FirebaseFirestore firebaseFirestore;
    /**
     * Reference to a specific document in the database.
     */
    private DocumentReference docRef;
    /**
     * Reference to a specific storage location in Firebase Storage.
     */
    private StorageReference storeRef;

    /**
     * Required empty public constructor
     */
    public ProfileFragment() {}

    /**
     * Called when the fragment is being created.
     * @param savedInstanceState The saved instance state bundle.
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        userId = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();
        imagePath = "images/" + userId + "_profile.jpg";

        docRef = firebaseFirestore.collection("users").document(userId);
        storeRef = FirebaseStorage.getInstance().getReference();
    }

    /**
     * Called when the fragment's view is created.
     * @param inflater The layout inflater.
     * @param container The container for the fragment's view.
     * @param savedInstanceState The saved instance state bundle.
     * @return The created view for the fragment.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profile, container, false);

        userName = view.findViewById(R.id.userName);
        userEmail = view.findViewById(R.id.userEmail);
        userImage = view.findViewById(R.id.userImage);

        docRef.addSnapshotListener(requireActivity(), (value, error) -> {
            if(value != null) {
                userName.setText(value.getString("name"));
                userEmail.setText(value.getString("email"));
            }
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

    /**
     * Uploads an image to Firebase Storage.
     * @param imageUri The URI of the image to upload.
     * @param imagePath The path where the image will be stored in Firebase Storage.
     */
    private void uploadImageToFirebaseStorage(Uri imageUri, String imagePath)
    {
        StorageReference fileRef = storeRef.child(imagePath);
        fileRef.putFile(imageUri);
    }

    /**
     * Callback method called when the activity launched for result returns a result.
     */
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