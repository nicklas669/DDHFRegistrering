package hyltofthansen.ddhfregistrering;

import android.app.Fragment;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class imageBrowse extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().setTitle("Vedhæft billede");

        //Aktiver ActionBar menu med Opret knap
        setHasOptionsMenu(true);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true); // aktivér "tilbage"-pil i venstre top

        View root = inflater.inflate(R.layout.imagebrowse, container, false);

        Button b_gallery = (Button) root.findViewById(R.id.imgBrowse_bGallery);
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
}
