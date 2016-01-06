package hyltofthansen.ddhfregistrering.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import hyltofthansen.ddhfregistrering.R;

public class ImageBrowseFragment extends Fragment {
    private static final int PICK_IMAGE = 100, REQUEST_TAKE_PHOTO = 99;
    ImageView iv_gallery;
    Uri imageUri;
    SharedPreferences prefs;
    private static final String TAG = "ImageBrowseFragment";
    private String mCurrentPhotoPath;
    private File photoFile;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().setTitle("Vedhæft billede");

        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true); // aktivér "tilbage"-pil i venstre top

        View root = inflater.inflate(R.layout.imagebrowse, container, false); // sæt layout op

        prefs = getActivity().getPreferences(Context.MODE_PRIVATE);
        iv_gallery = (ImageView) root.findViewById(R.id.imgBrowse_galleryView);

        // Læs om der er valgt et billede i forvejen
//        String imgURI = prefs.getString("chosenImage", null);
//        if (imgURI != null) {
//            // Hvis der er et valgt billede så vis det i imageViewet
//            try {
//                imageUri = Uri.parse(imgURI);
//                Bitmap img = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);
//                iv_gallery.setImageBitmap(img);
//                setHasOptionsMenu(true);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//        }

        Button b_gallery = (Button) root.findViewById(R.id.imgBrowse_bGallery);
        b_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent gallery = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                Intent gallery = new Intent(Intent.ACTION_OPEN_DOCUMENT).setType("image/*");
                startActivityForResult(gallery, PICK_IMAGE);
            }
        });

        Button b_newImg = (Button) root.findViewById(R.id.imgBrowse_bImage);
        b_newImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                // Ensure that there's a camera activity to handle the intent
                if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                    photoFile = null;

                    try {
                        photoFile = createImageFile();
                    } catch (IOException ex) {
                        Log.d(TAG, ex.toString());
                    }
                    // Continue only if the File was successfully created
                    if (photoFile != null) {
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                                Uri.fromFile(photoFile));
                        startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
                    }

                } else { // device har ikke kamera features
                    //Viser AlertDialog med teksten "Enheden har ikke kamerafunktionalitet"
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage(R.string.intetKameraDialogTekst).setTitle(R.string.intetKameraDialogTitel);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }
        });
        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == PICK_IMAGE) {
                imageUri = intent.getData();
                InputStream imageStream = null;
                try {
                    imageStream = getActivity().getContentResolver().openInputStream(imageUri);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                Bitmap imageBitmap = BitmapFactory.decodeStream(imageStream);

                iv_gallery.setImageBitmap(imageBitmap);
                //Aktiver ActionBar "OK" knap
//                setHasOptionsMenu(true);
            }
            else if (requestCode == REQUEST_TAKE_PHOTO) {
                galleryAddPic();
//                Bundle extras = intent.getExtras();
//                Bitmap imageBitmap = (Bitmap) extras.get("data");
                Bitmap imageBitmap = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
                iv_gallery.setImageBitmap(setPic());
                SharedPreferences.Editor prefedit = prefs.edit();
                prefedit.putString("chosenImage", photoFile.getAbsolutePath());
                prefedit.commit();
                //Aktiver ActionBar "OK" knap
//                setHasOptionsMenu(true);
            }

        }
    }

    /**
     * http://developer.android.com/training/camera/photobasics.html
     */
    private Bitmap setPic() {
        // Get the dimensions of the View
        int targetW = iv_gallery.getWidth();
        int targetH = iv_gallery.getHeight();

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(photoFile.getAbsolutePath(), bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(photoFile.getAbsolutePath(), bmOptions);
        return bitmap;
    }

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        getActivity().sendBroadcast(mediaScanIntent);
    }

    /**
     * http://developer.android.com/training/camera/photobasics.html
     * @return
     * @throws IOException
     */
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }

//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.action_confirmImage: //Der blev trykket på "OK" knappen i browseImage fragment
//                // Gem URI til valgt billede
//                SharedPreferences.Editor prefedit = prefs.edit();
////                prefedit.putString("chosenImage", imageUri.toString());
////                prefedit.commit();
//                // Hop tilbage til Opret genstand fragment
//                getFragmentManager().popBackStack();
//                break;
//        }
//        return true;
//    }
}