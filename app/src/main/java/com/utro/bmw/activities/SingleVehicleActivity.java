package com.utro.bmw.activities;

import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.utro.bmw.BMW;
import com.utro.bmw.R;
import com.utro.bmw.Utility;
import com.utro.bmw.adapters.VehicleOptionsAdapter;
import com.utro.bmw.model.GetListHandler;
import com.utro.bmw.model.ImageVehicle;
import com.utro.bmw.model.Vehicle;
import com.utro.bmw.model.VehicleOption;
import com.utro.bmw.utils.CustomScrollView;
import com.utro.bmw.utils.DataToSaveImagesVehicles;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class SingleVehicleActivity extends BaseActivity{

    private static int NUMBER_OF_PIXELS_TO_ROTATE = 30;  //сколько нужно проскроллить картинку, чтобы она повернулась на одно значение
    private static int ANGLE_PER_ROTATION = 10; //на сколько градусов поворачивается картинка

    TextView title, price;
    ImageView vehicleImg;
    ArrayList<VehicleOption> vehicleOptions;  //массив опций автомобиля
    Vehicle currentVehicle;
    ListView optionsList;
    ArrayList<ImageVehicle> imageVehicles;   //массив ссылок для каждого угла
    int currentAngle = 50, currentProgress = 0, screenWidth, screenHeight, totalTime = 0, imageWidthToDownload;
    float currentPositionX = 0, firstPositionX = 0;
    CustomScrollView scrollView;  //кастомный скроллвью, позваоляющий дизэйблить скроллинг
    ProgressBar progressBar;
    LinkedHashMap<String, Bitmap> imagesMap; //карта по принципу: ссылка->картинка в виде bitmap
    LinearLayout linearLayout;
    RelativeLayout relativeLayout;
    boolean orientationWasChanged = false;
    int threadNumber = 0;
    int orientation;

    @Override
    protected int getLayoutResourceIdentifier() {
        return R.layout.activity_test_vehicle;
    }

    @Override
    protected String getTitleToolBar() {
        return getString(R.string.title_activity_vehicles);
    }

    @Override
    protected boolean getDisplayHomeAsUp() {
        return true;
    }

    @Override
    protected boolean getHomeButtonEnabled() {
        return true;
    }

    @Override
    protected boolean getDisplayShowTitleEnabled() {
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getBaseContext();

        setToolBarThemeWhite();

        orientation = getScreenOrientation();

        vehicleImg = (ImageView) findViewById(R.id.vehicle_img);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        currentVehicle = getIntent().getParcelableExtra(VehiclesActivity.VEHICLE_KEY);

        getScreenDimensions();
        if (screenHeight > screenWidth) {
            imageWidthToDownload = screenHeight;
        } else {
            imageWidthToDownload = screenWidth;
        }

        imagesMap = new LinkedHashMap<>();
        imageVehicles = new ArrayList<>();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        DataToSaveImagesVehicles data = savedInstanceState.getParcelable("map");
        imagesMap = data.getMap();
        imageVehicles = savedInstanceState.getParcelableArrayList("arrayList");
        orientation = savedInstanceState.getInt("orientation");
        vehicleOptions = savedInstanceState.getParcelableArrayList("options");
        currentVehicle = savedInstanceState.getParcelable("currentVehicle");
    }

    @Override
    protected void onResume() {
        super.onResume();

        getScreenDimensions();

        if(getScreenOrientation() == Configuration.ORIENTATION_PORTRAIT){
            title = (TextView) findViewById(R.id.vehicle_title);
            price = (TextView) findViewById(R.id.vehicle_price);
            scrollView = (CustomScrollView) findViewById(R.id.test_drive_scroll);
            optionsList = (ListView) findViewById(R.id.details_list);
            linearLayout = (LinearLayout) findViewById(R.id.screen_ll);

            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(screenWidth, screenWidth * screenWidth / screenHeight);
            vehicleImg.setLayoutParams(params);

            title.setText(currentVehicle.getTitle());
            price.setText(currentVehicle.getPrice());

            if(vehicleOptions == null) {
                loadOptions(currentVehicle.getOrder());
            }
            else{
                optionsList.setAdapter(new VehicleOptionsAdapter(mContext, vehicleOptions));
                Drawable divider = getResources().getDrawable(R.drawable.divider);
                divider.setAlpha(128);
                optionsList.setDivider(divider);
                Utility.setListViewHeightBasedOnChildren(optionsList);
                mBar.setVisibility(View.INVISIBLE);
            }
        }
        else{
            relativeLayout = (RelativeLayout) findViewById(R.id.screen_ll);
        }
        currentAngle = 50;

        progressBar.setBackgroundColor(getResources().getColor(R.color.white));
        if(imageVehicles.size() == 0){
            loadImagesUrls(currentVehicle.getOrder(), imageWidthToDownload);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        DataToSaveImagesVehicles data = new DataToSaveImagesVehicles(imagesMap);
        outState.putParcelable("map", data);
        outState.putParcelableArrayList("arrayList", imageVehicles);
        outState.putInt("orientation", orientation);
        outState.putParcelableArrayList("options", vehicleOptions);
        outState.putParcelable("currentVehicle", currentVehicle);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onWindowFocusChanged(boolean focus) {
        super.onWindowFocusChanged(focus);

        if(orientation != getScreenOrientation()){
            orientation = getScreenOrientation();
            /*if(orientation == Configuration.ORIENTATION_LANDSCAPE){
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(screenWidth, vehicleImg.getHeight()); //(screenHeight - mToolBar.getHeight()) * 95 / 100
                vehicleImg.setLayoutParams(params);
            }*/
            orientationWasChanged = true;
            if(allImagesDownloaded()){
                //setImage(imageVehicles.get(currentAngle / ANGLE_PER_ROTATION).getUrl(), false);
                setImageTouchListener();
            }
            else{
                setFirstImageThread();
                checkForCompleteDownloadThread();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public int getScreenOrientation() {
        return getResources().getConfiguration().orientation;
    }

    private void loadOptions(String order) {

        mBar.setVisibility(View.VISIBLE);

        VehicleOption.getAllOptions(new GetListHandler<VehicleOption>() {
            @Override
            public void done(ArrayList<VehicleOption> data) {
                vehicleOptions = data;
                optionsList.setAdapter(new VehicleOptionsAdapter(mContext, vehicleOptions));
                Drawable divider = getResources().getDrawable(R.drawable.divider);
                divider.setAlpha(128);
                optionsList.setDivider(divider);
                mBar.setVisibility(View.INVISIBLE);
                Utility.setListViewHeightBasedOnChildren(optionsList);
            }

            @Override
            public void error(String responseError) {

            }
        }, order);
    }

    private void loadImagesUrls(String order, int width) {
        ImageVehicle.getImages(new GetListHandler<ImageVehicle>() {
            @Override
            public void done(ArrayList<ImageVehicle> data) {
                imageVehicles = data;
                setFirstImageThread();
                for (final ImageVehicle object : imageVehicles) {
                    if(object.getAngle() == currentAngle){
                        continue;
                    }
                    downloadImages(object);
                }
                checkForCompleteDownloadThread();
                //checkInternetConnectionThread();
            }

            @Override
            public void error(String responseError) {

            }
        }, order, width);
    }

    public void getScreenDimensions() {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        screenHeight = metrics.heightPixels;
        screenWidth = metrics.widthPixels;
    }

    public void downloadImages(ImageVehicle object) {

        final ImageVehicle imageObject = object;
        if ((imagesMap.get(imageObject.getUrl()) != null) || (imageObject.getAngle() == currentAngle)){
            return;
        }

        final Handler handler = new Handler();

        final Thread thread = new Thread(new Runnable() {
            String url = imageObject.getUrl();
            Bitmap bitmap;
            public void run() {
                threadNumber++;
                Log.d("Threads number", "Threads number = " + threadNumber);
                try {
                    bitmap = Glide.with(mContext).load(url).asBitmap().into(imageWidthToDownload, imageWidthToDownload / 3).get();
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d("Thread stopped", "" + e.getMessage());
                }

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (bitmap != null) {
                            imagesMap.put(url, bitmap);
                            progressBar.setProgress(currentProgress++);
                        }
                        else{
                            downloadImages(imageObject);
                        }
                    }
                });
            }
        });
        thread.start();
    }

    /*void startMyTask(AsyncTask asyncTask) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
            asyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, params);
        else
            asyncTask.execute(params);
    }*/

    public void setFirstImageThread() {  //загрузка первой картинки

        final int index = currentAngle / ANGLE_PER_ROTATION;
        if((imageVehicles.size() != 0) && (imageVehicles.get(index) != null) && (imagesMap.get(imageVehicles.get(index).getUrl()) != null)){
            setImage(imageVehicles.get(index).getUrl(), allImagesDownloaded());
        }
        else {
            final Handler handler = new Handler();
            Thread thread = new Thread(new Runnable() {
                Bitmap bitmap;
                @Override
                public void run() {
                    try {
                        Log.d("setFirstImageThread", "Starting thread");
                        bitmap = Glide.with(mContext).load(imageVehicles.get(index).getUrl()).asBitmap().into(imageWidthToDownload, imageWidthToDownload / 3).get();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (bitmap != null) {
                                imagesMap.put(imageVehicles.get(index).getUrl(), bitmap);
                                setImage(imageVehicles.get(index).getUrl(), true);
                                progressBar.setProgress(currentProgress++);
                            }
                            else{
                                downloadImages(imageVehicles.get(index));
                            }
                        }
                    });
                }
            });
            thread.start();
        }
    }

    public void setImage(String url, boolean isDownloading) {  //установка конкретной картинки в imageView
        /*if (imagesMap.get(url) == null) {
            downloadImages();
        }*/

        getScreenDimensions();

        Bitmap bitmap = imagesMap.get(url);
        if (bitmap != null) {
            setLayoutColorSimilarToImageBackground(bitmap);
        }

        Bitmap bmLeftArrow, bmRightArrow, resizedLeftArrow, resizedRightArrow, newBitmap;

        bmLeftArrow = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.arrow_exterior_left);
        bmRightArrow = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.arrow_exterior_right);

        int w = vehicleImg.getWidth();
        int h = vehicleImg.getHeight();
        Paint paint = setAlpha(isDownloading);

        if (getScreenOrientation() == Configuration.ORIENTATION_PORTRAIT) {

            bitmap = Bitmap.createScaledBitmap(bitmap, screenWidth, bitmap.getHeight() * screenWidth / screenHeight, false);
            Bitmap.Config config = bitmap.getConfig();
            newBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), config);
            Canvas newCanvas = new Canvas(newBitmap);
            newCanvas.drawBitmap(bitmap, 0, 0, null);

            if (BMW.getDisplayColumns(this).equals("normal")) {
                resizedLeftArrow = Bitmap.createScaledBitmap(bmLeftArrow, bitmap.getWidth() / 6, bitmap.getHeight() / 6, false);
                resizedRightArrow = Bitmap.createScaledBitmap(bmRightArrow, bitmap.getWidth() / 6, bitmap.getHeight() / 6, false);
            } else {
                resizedLeftArrow = Bitmap.createScaledBitmap(bmLeftArrow, bitmap.getWidth() / 10, bmLeftArrow.getHeight() * 3 / 4, false);
                resizedRightArrow = Bitmap.createScaledBitmap(bmRightArrow, bitmap.getWidth() / 10, bmRightArrow.getHeight() * 3 / 4, false);
            }

            newCanvas.drawBitmap(resizedRightArrow, screenWidth - resizedRightArrow.getWidth() - screenWidth / 40, h / 2 + bitmap.getHeight() / 10, paint);
            newCanvas.drawBitmap(resizedLeftArrow, screenWidth / 40, h / 2 + bitmap.getHeight() / 10, paint);
        } else {

            Bitmap.Config config = bitmap.getConfig();
            newBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), config);
            Canvas newCanvas = new Canvas(newBitmap);
            newCanvas.drawBitmap(bitmap, 0, 0, null);

            resizedLeftArrow = Bitmap.createScaledBitmap(bmLeftArrow, bitmap.getWidth() / 5, bitmap.getHeight() / 5, false);
            resizedRightArrow = Bitmap.createScaledBitmap(bmRightArrow, bitmap.getWidth() / 5, bitmap.getHeight() / 5, false);

            newCanvas.drawBitmap(resizedRightArrow, w - resizedRightArrow.getWidth() - w / 20, 3 * bitmap.getHeight() / 4, paint);
            newCanvas.drawBitmap(resizedLeftArrow, w / 20, 3 * bitmap.getHeight() / 4, paint);
        }

        vehicleImg.setImageBitmap(newBitmap);
    }

    public Paint setAlpha(boolean isDownloading) { //альфа для стрелочек
        Paint paint = new Paint();
        if (isDownloading) {
            paint.setAlpha(128);
        } else {
            paint.setAlpha(255);
        }

        return paint;
    }

    public void setLayoutColorSimilarToImageBackground(Bitmap bitmap) {  //задание цвета для бэкграунда
        int pixel = bitmap.getPixel(0, 0);
        int redValue = Color.red(pixel);
        int greenValue = Color.green(pixel);
        int blueValue = Color.blue(pixel);
        if(orientation == Configuration.ORIENTATION_PORTRAIT) {
            linearLayout.setBackgroundColor(Color.rgb(redValue, greenValue, blueValue));
        }
        else{
            relativeLayout.setBackgroundColor(Color.rgb(redValue, greenValue, blueValue));
        }
        progressBar.setBackgroundColor(Color.rgb(redValue, greenValue, blueValue));
    }

    public void checkForCompleteDownloadThread() { //каждую секунду чекает полную загрузку

        if((imagesMap.size() != imageVehicles.size()) || imageVehicles.size() == 0) {
            final Handler handler = new Handler();
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    do {
                        SystemClock.sleep(500);
                        totalTime++;
                        if ((imagesMap.size() == imageVehicles.size()) && (imageVehicles.size() != 0)) {
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    setImageTouchListener();
                                }
                            });
                        }
                    }
                    while (imagesMap.size() != imageVehicles.size());
                }
            });
            thread.start();
        }
    }

    public void turnLeft() {
        currentAngle += ANGLE_PER_ROTATION;
        if (currentAngle == 360) {
            currentAngle = 0;
        }
        setImage(imageVehicles.get(currentAngle / ANGLE_PER_ROTATION).getUrl(), false);
    }

    public void turnRight() {
        currentAngle -= ANGLE_PER_ROTATION;
        if (currentAngle == -10) {
            currentAngle = 350;
        }
        setImage(imageVehicles.get(currentAngle / ANGLE_PER_ROTATION).getUrl(), false);
    }

    public void setImageTouchListener(){
        progressBar.setVisibility(View.GONE);
        setImage(imageVehicles.get(currentAngle / ANGLE_PER_ROTATION).getUrl(), false);

        vehicleImg.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (scrollView != null) {
                    scrollView.setEnableScrolling(false);
                }
                float x = event.getRawX();
                switch (event.getAction()) {
                    case MotionEvent.ACTION_MOVE:
                        currentPositionX = x;
                        break;
                    case MotionEvent.ACTION_DOWN:
                        firstPositionX = x;
                        break;
                    case MotionEvent.ACTION_UP:
                        if (scrollView != null) {
                            scrollView.setEnableScrolling(true);
                        }
                        break;
                }

                if (currentPositionX - firstPositionX > NUMBER_OF_PIXELS_TO_ROTATE) {
                    turnRight();
                    firstPositionX += NUMBER_OF_PIXELS_TO_ROTATE;
                }
                if (currentPositionX - firstPositionX < -NUMBER_OF_PIXELS_TO_ROTATE) {
                    turnLeft();
                    firstPositionX -= NUMBER_OF_PIXELS_TO_ROTATE;
                }

                return true;
            }
        });
    }

    public boolean allImagesDownloaded(){
        return ((imagesMap.size() == imageVehicles.size()) && (imageVehicles.size() != 0));
    }
}
