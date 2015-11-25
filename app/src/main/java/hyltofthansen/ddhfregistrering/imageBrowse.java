package hyltofthansen.ddhfregistrering;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

public class imageBrowse extends Fragment {
    private static final int PICK_IMAGE = 100;
    ImageView iv_gallery;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().setTitle("Vedhæft billede");

        //Aktiver ActionBar menu
        setHasOptionsMenu(true);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true); // aktivér "tilbage"-pil i venstre top

        View root = inflater.inflate(R.layout.imagebrowse, container, false);

        iv_gallery = (ImageView) root.findViewById(R.id.imgBrowse_galleryView);
        Button b_gallery = (Button) root.findViewById(R.id.imgBrowse_bGallery);
        b_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gallery =
                        new Intent(Intent.ACTION_PICK,
                                android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                startActivityForResult(gallery, PICK_IMAGE);
            }
        });

        Button b_newImg = (Button) root.findViewById(R.id.imgBrowse_bImage);
        b_newImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) { // device har kamera feature
                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                        startActivityForResult(takePictureIntent, 1);
                    }
                } else { // device har ikke kamera features
                    System.out.println("Device har ikke camera feature!!");
                    // vis en alertdialog her der siger, at kamera ikke er tilgængelig?
                    // ALTERNATIVT: LAD VÆRE MED AT VISE IMAGEBUTTON OG IMAGEVIEW HVIS DEVICE IKKE HAR CAMERA FEATURE?
                }
            }
        });

        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == PICK_IMAGE) {
            Uri imageUri = data.getData();
            try {
                Bitmap img = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);
                iv_gallery.setImageBitmap(img);
                //iv_gallery.invalidate();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //Inflate ActionBar for OK knap
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_confirmimg, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_confirmImage: //Der blev trykket på "OK" knappen i browseImage fragment
                // Gør noget her
                break;
        }
        return true;
    }
}
