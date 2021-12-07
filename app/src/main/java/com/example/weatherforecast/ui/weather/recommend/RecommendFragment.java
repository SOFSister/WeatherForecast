package com.example.weatherforecast.ui.weather.recommend;

import static android.app.Activity.RESULT_OK;

import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.weatherforecast.R;
import com.example.weatherforecast.createQRCode.QRCode;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.File;
import java.util.Hashtable;

public class RecommendFragment extends Fragment {

    private RecommendViewModel mViewModel;


    private Button createQRCodeBtn;
    private EditText createQRCodeEditText;
    private ImageView createQRCodeImg;

    private Button photoBtn;
    private ImageView photoImage;
    private Uri imageUri;
    private String path= Environment.getExternalStorageDirectory()+ File.separator+"DCIM"+File.separator+"Camera"+File.separator+"temp.jpg";

    public static RecommendFragment newInstance() {
        return new RecommendFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.recommend_fragment, container, false);
        createQRCodeBtn=view.findViewById(R.id.create_QRcode_btn);
        createQRCodeEditText=view.findViewById(R.id.create_QRcode_text);
        createQRCodeImg=view.findViewById(R.id.create_QRcode_Img);
        initQRCode();
        createQRCodeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Resources res = getResources();
                Bitmap logoBitmap= BitmapFactory.decodeResource(res,R.drawable.logo);
                Bitmap aimQRCode=QRCode.createQRCodeBitmap(createQRCodeEditText.getText().toString(), 800, 800,"UTF-8","H", "1", Color.BLACK, Color.WHITE,logoBitmap,0.5F);
                createQRCodeImg.setImageBitmap(aimQRCode);
            }
        });


        photoBtn = view.findViewById(R.id.photo_btn);
        photoImage = view.findViewById(R.id.photo);
        photoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent,10);
            }
        });
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 10:
                if (resultCode == RESULT_OK) {
                    Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                    photoImage.setImageBitmap(bitmap);
                }
                break;
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(RecommendViewModel.class);
        // TODO: Use the ViewModel
    }
    private void initQRCode(){
        Resources res = getResources();
        Bitmap logoBitmap= BitmapFactory.decodeResource(res,R.drawable.logo);
        Bitmap aimQRCode=QRCode.createQRCodeBitmap("www.hznu.edu.cn", 800, 800,"UTF-8","H", "1", Color.BLACK, Color.WHITE,logoBitmap,0.5F);
        createQRCodeImg.setImageBitmap(aimQRCode);
    }
}