package com.track.trackandtrigger;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Profile#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Profile extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    TextView email_text;
    TextView name_text;
    CircleImageView profile_dp;
    View personal_info;
    View share;
    View settings;
    View help;
    Button sign_out;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Profile() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Profile.
     */
    // TODO: Rename and change types and number of parameters
    public static Profile newInstance(String param1, String param2) {
        Profile fragment = new Profile();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View profileInflater = inflater.inflate(R.layout.fragment_profile, container, false);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        email_text = profileInflater.findViewById(R.id.email_text);
        name_text = profileInflater.findViewById(R.id.name_text);
        profile_dp = profileInflater.findViewById(R.id.profile_pic);
        personal_info = profileInflater.findViewById(R.id.personal_info);
        share = profileInflater.findViewById(R.id.share_app);
        settings = profileInflater.findViewById(R.id.settings);
        help = profileInflater.findViewById(R.id.help);
        sign_out = profileInflater.findViewById(R.id.sign_out_btn);
        email_text.setText(user.getEmail());
        name_text.setText(user.getDisplayName());
        if(user.getPhotoUrl()!=null) {
            Glide.with(this)
                    .load(user.getPhotoUrl())
                    .into(profile_dp);
        }
        personal_info.setOnClickListener(v -> {
            Toast.makeText(getActivity(), "Profile", Toast.LENGTH_SHORT).show();
        });
        share.setOnClickListener(v -> {
            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            String shareBody = "*Track and Trigger*\nHey there!\nTrack and Trigger helps you keep track of your day to day needs. I find this app very useful. \nGet this from https://example.com";
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT,shareBody);
            startActivity(Intent.createChooser(sharingIntent,"Share via"));
        });
        settings.setOnClickListener(v -> {
            Toast.makeText(getActivity(), "Settings", Toast.LENGTH_SHORT).show();
        });
        help.setOnClickListener(v -> {
            String url = "https://example.com";
            Intent urlIntent = new Intent(Intent.ACTION_VIEW);
            urlIntent.setData(Uri.parse(url));
            startActivity(urlIntent);
        });
        sign_out.setOnClickListener(v -> {
            AuthUI.getInstance()
                    .signOut(getActivity())
                    .addOnCompleteListener(task -> {
                        startActivity(new Intent(getActivity(),SplashScreenActivity.class));
                    });
        });
        // Inflate the layout for this fragment
        return profileInflater;
    }
}