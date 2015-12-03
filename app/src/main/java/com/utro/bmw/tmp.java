package com.utro.bmw;

/**
 * Created by 123 on 29.05.2015.
 */
/*public class tmp extends BaseActivity implements View.OnClickListener {

    private static int NUMBER_OF_PIXELS_TO_ROTATE = 30;  //сколько нужно проскроллить картинку, чтобы она повернулась на одно значение
    private static int ANGLE_PER_ROTATION = 10; //на сколько градусов поворачивается картинка

    TextView title, price;
    ImageView vehicleImg;
    ArrayList<VehicleOption> vehicleOptions;  //массив опций автомобиля
    Vehicle currentVehicle;
    ListView optionsList;
    CircularProgressBar bar;
    ArrayList<ImageVehicle> imageVehicles;   //массив ссылок для каждого угла
    int currentAngle = 50, currentProgress = 0, screenWidth, screenHeight, totalTime = 0, imageWidthToDownload;
    float currentPositionX = 0, firstPositionX = 0;
    CustomScrollView scrollView;  //кастомный скроллвью, позваоляющий дизэйблить скроллинг
    ProgressBar progressBar;
    LinkedHashMap<String, byte[]> imagesMap; //карта по принципу: ссылка->картинка в виде массива байтов
    LinearLayout linearLayout;
    boolean orientationWasChanged = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!orientationWasChanged) {
            title = (TextView) findViewById(R.id.vehicle_title);
            price = (TextView) findViewById(R.id.vehicle_price);
            vehicleImg = (ImageView) findViewById(R.id.vehicle_img);
            linearLayout = (LinearLayout) findViewById(R.id.screen_ll);
            scrollView = (CustomScrollView) findViewById(R.id.test_drive_scroll);
            progressBar = (ProgressBar) findViewById(R.id.progressBar);
        }

        imagesMap = new LinkedHashMap<>();
        imageVehicles = new ArrayList<>();

        scrollView.setEnableScrolling(true);

        bar = (CircularProgressBar) findViewById(R.id.bar);

        //currentVehicle = getIntent().getParcelableExtra(VehiclesActivity.VEHICLE_KEY);

        title.setText(currentVehicle.getTitle());
        price.setText("Цена " + currentVehicle.getPrice());

        optionsList = (ListView) findViewById(R.id.details_list);
        loadOptions(currentVehicle.getOrder());

        getScreenDimensions();

        if (!orientationWasChanged) {
            if (screenHeight > screenWidth) {
                imageWidthToDownload = screenHeight;
            } else {
                imageWidthToDownload = screenWidth;
            }
        }

        if (getScreenOrientation() == Configuration.ORIENTATION_LANDSCAPE) {
            setContentView(R.layout.activity_test_vehicle_horizontal);
            vehicleImg = (ImageView) findViewById(R.id.vehicle_img_horizontal);
            linearLayout = (LinearLayout) findViewById(R.id.vehicle_horizontal_ll);
            progressBar = (ProgressBar) findViewById(R.id.progressBarHorizontal);
        }

        loadImages(currentVehicle.getOrder(), imageWidthToDownload - imageWidthToDownload / 20); //screenHeight - screenHeight / 20
    }

    public void getScreenDimensions() {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        screenHeight = metrics.heightPixels;
        screenWidth = metrics.widthPixels;
    }

    @Override
    public void onClick(View view) {

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

    @Override
    protected int getLayoutResourceIdentifier() {
        return R.layout.activity_test_vehicle123;
    }

    @Override
    protected String getTitleToolBar() {
        return getString(R.string.vehicles);
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadOptions(String order) {

        bar.setVisibility(View.VISIBLE);

        VehicleOption.getAllOptions(new GetListHandler<VehicleOption>() {
            @Override
            public void done(ArrayList<VehicleOption> data) {
                vehicleOptions = data;
                optionsList.setAdapter(new VehicleOptionsAdapter(mContext, vehicleOptions));

                bar.setVisibility(View.INVISIBLE);
                Utility.setListViewHeightBasedOnChildren(optionsList);
            }

            @Override
            public void error(String responseError) {

            }
        }, order);
    }

    private void loadImages(String order, int width) {
        ImageVehicle.getImages(new GetListHandler<ImageVehicle>() {
            @Override
            public void done(ArrayList<ImageVehicle> data) {
                imageVehicles = data;

                downloadImages(false);
                setFirstImageThread();
                checkForCompleteDownloadThread();
                checkInternetConnectionThread();
            }

            @Override
            public void error(String responseError) {

            }
        }, order, width);
    }

    public void setLayoutColorSimilarToImageBackground(Bitmap bitmap) {  //задание цвета для бэкграунда
        int pixel = bitmap.getPixel(0, 0);
        int redValue = Color.red(pixel);
        int greenValue = Color.green(pixel);
        int blueValue = Color.blue(pixel);
        linearLayout.setBackgroundColor(Color.rgb(redValue, greenValue, blueValue));
        progressBar.setBackgroundColor(Color.rgb(redValue, greenValue, blueValue));
    }

    public void setImage(String url, boolean isDownloading) {  //установка конкретной картинки в imageView
        if (imagesMap.get(url) == null) {
            downloadImages(false);
        }

        Bitmap bitmap = BitmapFactory.decodeByteArray(imagesMap.get(url), 0, imagesMap.get(url).length);
        if (bitmap != null) {
            setLayoutColorSimilarToImageBackground(bitmap);
        }

        Bitmap bmLeftArrow, bmRightArrow;
        Bitmap newBitmap;

        bmLeftArrow = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.arrow_exterior_left);
        bmRightArrow = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.arrow_exterior_right);

        if (getScreenOrientation() == Configuration.ORIENTATION_PORTRAIT) {
            int w = vehicleImg.getWidth();
            int h = vehicleImg.getHeight();

            Bitmap.Config config = bitmap.getConfig();
            newBitmap = Bitmap.createBitmap(w, h, config);
            Canvas newCanvas = new Canvas(newBitmap);

            Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, screenWidth - screenWidth / 20, 220, false);
            newCanvas.drawBitmap(resizedBitmap, screenWidth / 40, bitmap.getHeight() / 10, null);

            Paint paint = setAlpha(isDownloading);

            Bitmap resizedLeftArrow = Bitmap.createScaledBitmap(bmLeftArrow, bitmap.getWidth() / 5, bmLeftArrow.getHeight() * 3 / 4, false);
            Bitmap resizedRightArrow = Bitmap.createScaledBitmap(bmRightArrow, bitmap.getWidth() / 5, bmRightArrow.getHeight() * 3 / 4, false);

            newCanvas.drawBitmap(resizedRightArrow, w - resizedRightArrow.getWidth() - screenWidth / 40, h / 2 + bitmap.getHeight() / 30, paint);  //bitmap.getHeight() / 2 + screenHeight / 30 + bitmap.getHeight() / 10
            newCanvas.drawBitmap(resizedLeftArrow, screenWidth / 40, h / 2 + bitmap.getHeight() / 30, paint);
            vehicleImg.setImageBitmap(newBitmap);
        } else {
            int w = vehicleImg.getWidth();
            int h = vehicleImg.getHeight();

            Bitmap.Config config = bitmap.getConfig();
            newBitmap = Bitmap.createBitmap(w, h, config);
            Canvas newCanvas = new Canvas(newBitmap);

            Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, screenWidth - screenWidth / 20, bitmap.getHeight(), false);
            newCanvas.drawBitmap(resizedBitmap, 0, -screenWidth / 20, null);

            Paint paint = setAlpha(isDownloading);

            Bitmap resizedLeftArrow = Bitmap.createScaledBitmap(bmLeftArrow, bmLeftArrow.getWidth() / 2, bmLeftArrow.getHeight(), false);
            Bitmap resizedRightArrow = Bitmap.createScaledBitmap(bmRightArrow, bmRightArrow.getWidth() / 2, bmRightArrow.getHeight(), false);

            newCanvas.drawBitmap(resizedRightArrow, resizedBitmap.getWidth() - resizedRightArrow.getWidth() - screenWidth / 48 + screenWidth / 20, bitmap.getHeight() / 2 + screenHeight / 30, paint);
            newCanvas.drawBitmap(resizedLeftArrow, screenWidth / 48, bitmap.getHeight() / 2 + screenHeight / 30, paint);
            vehicleImg.setImageBitmap(newBitmap);
        }
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

    public void downloadImages(boolean downloadAfterBeingDisconnected) {

        if (downloadAfterBeingDisconnected) {
            imagesMap.clear();
            currentProgress = 0;
            progressBar.setProgress(currentProgress);
            progressBar.setVisibility(View.VISIBLE);
            setFirstImageThread();
            checkForCompleteDownloadThread();
        }

        for (final ImageVehicle object : imageVehicles) {

            if (imagesMap.get(object.getUrl()) != null) {
                continue;
            }

            final Handler handler = new Handler();

            Thread thread = new Thread(new Runnable() {
                byte[] byteArray = null;
                String url = object.getUrl();
                int imageLength
                        ,
                        bytesRead = 0;

                public void run() {
                    try {
                        HttpParams httpParameters = new BasicHttpParams();
                        int timeoutConnection = 3000;

                        HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
                        int timeoutSocket = 5000;

                        HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);

                        DefaultHttpClient client = new DefaultHttpClient();
                        client.setParams(httpParameters);
                        //HttpGet request = new HttpGet(url);
                        HttpGet httpGet = new HttpGet(url);
                        HttpResponse response = client.execute(httpGet);
                        HttpEntity entity = response.getEntity();
                        imageLength = (int) (entity.getContentLength());
                        InputStream is = entity.getContent();

                        Log.d("imageLength", "imageLength  = " + imageLength);

                        byteArray = new byte[imageLength];
                        while (bytesRead < imageLength) {
                            int n = is.read(byteArray, bytesRead, imageLength - bytesRead);
                            if (n <= 0) {
                                Log.d("error", "error");
                            }
                            bytesRead += n;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.d("Thread stopped", "" + e.getMessage());
                    }

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            currentProgress++;
                            progressBar.setProgress(currentProgress);
                            if ((byteArray != null) && (bytesRead == imageLength)) {
                                imagesMap.put(url, byteArray);
                            }
                        }
                    });
                }
            });
            thread.start();
        }
    }

    public void checkForCompleteDownloadThread() { //каждую секунду чекает полную загрузку
        final Handler handler = new Handler();

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                do {
                    SystemClock.sleep(500);
                    totalTime++;
                    if (imagesMap.size() == imageVehicles.size()) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                progressBar.setVisibility(View.GONE);
                                setImage(imageVehicles.get(currentAngle / ANGLE_PER_ROTATION).getUrl(), false);

                                vehicleImg.setOnTouchListener(new View.OnTouchListener() {
                                    @Override
                                    public boolean onTouch(View v, MotionEvent event) {
                                        scrollView.setEnableScrolling(false);
                                        float x = event.getRawX();
                                        switch (event.getAction()) {
                                            case MotionEvent.ACTION_MOVE:
                                                currentPositionX = x;
                                                break;
                                            case MotionEvent.ACTION_DOWN:
                                                firstPositionX = x;
                                                break;
                                            case MotionEvent.ACTION_UP:
                                                scrollView.setEnableScrolling(true);
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
                        });
                    }
                    if (totalTime > 5) { //каждые пять секунд перезагрузка незагруженных картинок
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                downloadImages(false);
                            }
                        });
                    }
                }
                while (imagesMap.size() != imageVehicles.size());
            }
        });
        thread.start();
    }

    public void setFirstImageThread() {  //загрузка первой картинки
        final Handler handler = new Handler();

        Thread thread = new Thread(new Runnable() {
            byte[] byteArray = null;

            @Override
            public void run() {
                try {
                    DefaultHttpClient client = new DefaultHttpClient();
                    HttpGet request = new HttpGet(imageVehicles.get(currentAngle / ANGLE_PER_ROTATION).getUrl());
                    HttpResponse response = client.execute(request);
                    HttpEntity entity = response.getEntity();
                    int imageLength = (int) (entity.getContentLength());
                    InputStream is = entity.getContent();

                    byteArray = new byte[imageLength];
                    int bytesRead = 0;
                    while (bytesRead < imageLength) {
                        int n = is.read(byteArray, bytesRead, imageLength - bytesRead);
                        if (n <= 0) {
                            Log.d("error", "error");
                        }
                        bytesRead += n;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (byteArray != null) {
                            imagesMap.put(imageVehicles.get(currentAngle / ANGLE_PER_ROTATION).getUrl(), byteArray);
                        }
                        setImage(imageVehicles.get(currentAngle / ANGLE_PER_ROTATION).getUrl(), true);
                    }
                });
            }
        });
        thread.start();
    }

    public static boolean isNetworkAvailable(Context context) {
        return ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo() != null;
    }

    public void checkInternetConnectionThread() { //чекаем, включен ли интернет, пока не загружены все картинки
        final Handler handler = new Handler();

        final Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                do {
                    SystemClock.sleep(1000);
                    if (!isNetworkAvailable(mContext)) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                vehicleImg.setImageResource(getResources().getColor(R.color.white));
                                Toast.makeText(mContext, "Потеряно интернет-соединение", Toast.LENGTH_SHORT).show();
                                checkForReconnection();
                                vehicleImg.setOnTouchListener(null);
                                progressBar.setVisibility(View.GONE);
                            }
                        });
                    }
                }
                while (isNetworkAvailable(mContext) && (imagesMap.size() != imageVehicles.size()));
            }
        });
        thread.start();
    }

    public void checkForReconnection() { //чекаем переподключение и возобновляем все загрузки
        final Handler handler = new Handler();

        final Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                do {
                    SystemClock.sleep(1000);
                    if (isNetworkAvailable(mContext)) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                downloadImages(true);
                                checkInternetConnectionThread();
                                Toast.makeText(mContext, "Возобновление загрузки", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } while (!isNetworkAvailable(mContext));
            }
        });
        thread.start();
    }

    public int getScreenOrientation() {
        return getResources().getConfiguration().orientation;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        MapImagesVehicles map = new MapImagesVehicles(imagesMap);
        outState.putParcelable("map", map);
        orientationWasChanged = true;
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        MapImagesVehicles map = savedInstanceState.getParcelable("map");
        imagesMap = map.getMap();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        orientationWasChanged = true;
        getScreenDimensions();

        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setContentView(R.layout.activity_test_vehicle_horizontal);
            vehicleImg = (ImageView) findViewById(R.id.vehicle_img);
            linearLayout = (LinearLayout) findViewById(R.id.vehicle_horizontal_ll);
            progressBar = (ProgressBar) findViewById(R.id.progressBarHorizontal);
            currentAngle = 50;
            //loadImages(currentVehicle.getOrder(), imageWidthToDownload - imageWidthToDownload / 20);
            downloadImages(false);
            checkForCompleteDownloadThread();
        } else {
            setContentView(R.layout.activity_test_vehicle123);
            title = (TextView) findViewById(R.id.vehicle_title);
            price = (TextView) findViewById(R.id.vehicle_price);
            vehicleImg = (ImageView) findViewById(R.id.vehicle_img);
            linearLayout = (LinearLayout) findViewById(R.id.screen_ll);
            scrollView = (CustomScrollView) findViewById(R.id.test_drive_scroll);
            progressBar = (ProgressBar) findViewById(R.id.progressBar);

            currentAngle = 50;

            //currentVehicle = getIntent().getParcelableExtra(VehiclesActivity.VEHICLE_KEY);

            title.setText(currentVehicle.getTitle());
            price.setText("Цена " + currentVehicle.getPrice());
            bar = (CircularProgressBar) findViewById(R.id.bar);
            bar.setVisibility(View.GONE);

            optionsList = (ListView) findViewById(R.id.details_list);
            loadOptions(currentVehicle.getOrder());
            //loadImages(currentVehicle.getOrder(), imageWidthToDownload - imageWidthToDownload / 20);
            downloadImages(false);
            checkForCompleteDownloadThread();
        }
  }
}*/
