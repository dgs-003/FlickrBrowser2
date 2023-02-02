package com.example.flickrbrowser2;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.flickrbrowser2.databinding.ActivityPhotoDetailBinding;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

public class PhotoDetailActivity extends BaseActivity {
    private static final String TAG = "PhotoDetailActivity";
    private AppBarConfiguration appBarConfiguration;
    private ActivityPhotoDetailBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_detail);
activateToolBar(true);
//        binding = ActivityPhotoDetailBinding.inflate(getLayoutInflater());
//        setContentView(binding.getRoot());
//
//        setSupportActionBar(binding.toolbar);
        Intent intent =getIntent();
        Photo photo =(Photo) intent.getSerializableExtra(PHOTO_TRANSFER);
        if(photo!=null)
        {
            TextView photo_author=(TextView) findViewById(R.id.photo_author);
            photo_author.setText(photo.getAuthor());

            TextView photo_title=(TextView) findViewById(R.id.photo_title);
            photo_title.setText(photo.getTitle());

            TextView photo_tags=(TextView) findViewById(R.id.photo_tags);
            photo_tags.setText(photo.getTags());

            ImageView imageView=(ImageView) findViewById(R.id.imageView);
            Picasso.get().load(photo.getLink())
                    .error(R.drawable.placeholder)
                    .placeholder(R.drawable.placeholder)
                    .into(imageView);
        }


    }


}