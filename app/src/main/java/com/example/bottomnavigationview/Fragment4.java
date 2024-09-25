package com.example.bottomnavigationview;

import static android.content.Context.WINDOW_SERVICE;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.zxing.WriterException;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class Fragment4 extends Fragment {

    TextView fname, lname, em, lo, ep, rb;
    ImageView qrimg;
    Bitmap bitmap;
    QRGEncoder qrgEncoder;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment4_profile,container,false);
        fname = (TextView) rootView.findViewById(R.id.firstName);
        lname = (TextView) rootView.findViewById(R.id.lastName);
        em = (TextView) rootView.findViewById(R.id.eMail);
        lo = (TextView) rootView.findViewById(R.id.logoutBtn);
        ep = (TextView) rootView.findViewById(R.id.edit_profile);
        rb = (TextView) rootView.findViewById(R.id.resbakunaBtn);

        qrimg = (ImageView) rootView.findViewById(R.id.idIVQrcode);

        rb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri webpage = Uri.parse("https://vaxcert.doh.gov.ph");

                Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
                startActivity(intent);
            }
        });




        ep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),EditProf.class));
            }
        });

        lo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getActivity(),MainActivity.class));
            }
        });




            DocumentReference docRef = db.collection("userDB").document(user.getUid());
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            fname.setText(document.getString("fname"));
                            lname.setText(document.getString("lname"));
                            em.setText(document.getString("email"));

                            // below line is for getting
                            // the windowmanager service.
                            WindowManager manager = (WindowManager) getActivity().getSystemService(WINDOW_SERVICE);

                            // initializing a variable for default display.
                            Display display = manager.getDefaultDisplay();

                            // creating a variable for point which
                            // is to be displayed in QR Code.
                            Point point = new Point();
                            display.getSize(point);

                            // getting width and
                            // height of a point
                            int width = point.x;
                            int height = point.y;

                            // generating dimension from width and height.
                            int dimen = width < height ? width : height;
                            dimen = dimen * 3 / 4;

                            // setting this dimensions inside our qr code
                            // encoder to generate our qr code.
                            qrgEncoder = new QRGEncoder(user.getUid(), null, QRGContents.Type.TEXT, dimen);
                            try {
                                // getting our qrcode in the form of bitmap.
                                bitmap = qrgEncoder.encodeAsBitmap();
                                // the bitmap is set inside our image
                                // view using .setimagebitmap method.
                                qrimg.setImageBitmap(bitmap);
                            } catch (WriterException e) {
                                // this method is called for
                                // exception handling.
                                Log.d("Tag", e.toString());
                            }

                        } else {

                        }
                    } else {

                    }
                }
            });


        return rootView;
    };


}


