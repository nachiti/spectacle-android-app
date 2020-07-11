package com.example.spectacleapp;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PointF;
import android.graphics.Rect;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.interpolator.view.animation.FastOutSlowInInterpolator;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.example.spectacleapp.converter.SpectacleConverter;
import com.example.spectacleapp.models.Spectacle;
import com.example.spectacleapp.service.ServiceGenerator;
import com.example.spectacleapp.service.SpectacleService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.mapbox.android.core.location.LocationEngine;
import com.mapbox.android.core.location.LocationEngineCallback;
import com.mapbox.android.core.location.LocationEngineProvider;
import com.mapbox.android.core.location.LocationEngineRequest;
import com.mapbox.android.core.location.LocationEngineResult;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.api.directions.v5.models.DirectionsResponse;
import com.mapbox.api.directions.v5.models.DirectionsRoute;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.Geometry;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.location.modes.RenderMode;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.plugins.annotation.SymbolManager;
import com.mapbox.mapboxsdk.plugins.annotation.SymbolOptions;
import com.mapbox.mapboxsdk.style.expressions.Expression;
import com.mapbox.mapboxsdk.style.layers.CircleLayer;
import com.mapbox.mapboxsdk.style.layers.Layer;
import com.mapbox.mapboxsdk.style.layers.Property;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncher;
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncherOptions;
import com.mapbox.services.android.navigation.ui.v5.route.NavigationMapRoute;
import com.mapbox.services.android.navigation.v5.navigation.NavigationRoute;
import com.squareup.picasso.Picasso;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

import static android.view.View.GONE;
import static androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_IDLE;
import static com.mapbox.mapboxsdk.style.expressions.Expression.eq;
import static com.mapbox.mapboxsdk.style.expressions.Expression.exponential;
import static com.mapbox.mapboxsdk.style.expressions.Expression.get;
import static com.mapbox.mapboxsdk.style.expressions.Expression.interpolate;
import static com.mapbox.mapboxsdk.style.expressions.Expression.literal;
import static com.mapbox.mapboxsdk.style.expressions.Expression.match;
import static com.mapbox.mapboxsdk.style.expressions.Expression.stop;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.circleColor;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.circleOpacity;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.circleRadius;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconAllowOverlap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconAnchor;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconImage;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconOffset;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconSize;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.visibility;
import static com.mapbox.mapboxsdk.style.layers.Property.NONE;
import static com.mapbox.mapboxsdk.style.layers.Property.VISIBLE;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback,
        PermissionsListener, MapboxMap.OnMapClickListener {

    // Variables needed to set symbolLayerActivity
    private static final String ICON_ID = "ICON_ID";
    private static final String LAYER_ID = "LAYER_ID";
    private static final String SOURCE_ID = "mapbox.poi";
    private static final String LOADING_LAYER_ID = "mapbox.poi.loading";
    private static final String CALLOUT_LAYER_ID = "mapbox.poi.callout";

    private static final String PROPERTY_SELECTED = "selected";
    private static final String PROPERTY_LOADING = "loading";
    private static final String PROPERTY_LOADING_PROGRESS = "loading_progress";
    private static final String PROPERTY_ID = "id";
    private static final String PROPERTY_FAVOURITE = "favourite";

    private static final long CAMERA_ANIMATION_TIME = 1950;
    private static final float LOADING_CIRCLE_RADIUS = 60;
    private static final int LOADING_PROGRESS_STEPS = 25; //number of steps in a progress animation

    private MapView mapView;
    private MapboxMap mapboxMap;
    private RecyclerView recyclerView;
    private Button btnStartNav;
    private Button btnCancelNav;
    private boolean routeNavigationIsVisible = false;

    private LocationComponent locationComponent;
    private PermissionsManager permissionsManager;
    private GeoJsonSource source;
    private FeatureCollection featureCollection;
    private DirectionsRoute currentRoute;
    private NavigationMapRoute navigationMapRoute;

    private HashMap<String, View> viewMap;
    private AnimatorSet animatorSet;

    // Variables needed to add the location engine
    private LocationEngine locationEngine;
    private long DEFAULT_INTERVAL_IN_MILLISECONDS = 1000L;
    private long DEFAULT_MAX_WAIT_TIME = DEFAULT_INTERVAL_IN_MILLISECONDS * 5;
    // Variables needed to listen to location updates
    private MapActivityLocationCallback callback = new MapActivityLocationCallback(this);

    @MapActivity.ActivityStep
    private int currentStep;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({STEP_INITIAL, STEP_LOADING, STEP_READY})
    public @interface ActivityStep {
    }

    private static final int STEP_INITIAL = 0;
    private static final int STEP_LOADING = 1;
    private static final int STEP_READY = 2;

    private static final Map<Integer, Double> stepZoomMap = new HashMap<>();

    static {
        stepZoomMap.put(STEP_INITIAL, 11.0);
        stepZoomMap.put(STEP_LOADING, 13.5);
        stepZoomMap.put(STEP_READY, 18.0);
    }

    //Appel à la class service
    public static SpectacleService spectacleService = ServiceGenerator.createService(SpectacleService.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Mapbox access token is configured here. This needs to be called either in your application
        // object or in the same activity which contains the mapview.
        Mapbox.getInstance(this, getString(R.string.access_token));
        // This contains the MapView in XML and needs to be called after the access token is configured.
        setContentView(R.layout.activity_map);

        recyclerView = findViewById(R.id.rv_on_top_of_map);
        btnStartNav = findViewById(R.id.btn_start_nav);
        btnCancelNav = findViewById(R.id.btn_cancel_nav);

        // Initialize the map view
        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        btnCancelNav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToMiniDetatilView();
            }
        });

        btnStartNav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startNavigation();
            }
        });

    }

    @Override
    public void onMapReady(@NonNull final MapboxMap mapboxMap) {

        this.mapboxMap = mapboxMap;

        mapboxMap.setStyle(Style.MAPBOX_STREETS
                , new Style.OnStyleLoaded() {
                    @Override
                    public void onStyleLoaded(@NonNull Style style) {
                        enableLocationComponent(style);

                        mapboxMap.getUiSettings().setCompassEnabled(true);
                        mapboxMap.getUiSettings().setLogoEnabled(false);
                        mapboxMap.getUiSettings().setAttributionEnabled(false);
                        new LoadPoiDataTask(MapActivity.this).execute(); //etape 1
                        mapboxMap.addOnMapClickListener(MapActivity.this);

                        //**FIN onStyleLoaded**//
                    }
                });

    }


    /**
     * Initialize the Maps SDK's LocationComponent
     */
    @SuppressWarnings({"MissingPermission"})
    private void enableLocationComponent(@NonNull Style loadedMapStyle) {
        // Check if permissions are enabled and if not request
        if (PermissionsManager.areLocationPermissionsGranted(this)) {

            // Get an instance of the component
             locationComponent = mapboxMap.getLocationComponent();

            // Set the LocationComponent activation options
            LocationComponentActivationOptions locationComponentActivationOptions =
                    LocationComponentActivationOptions.builder(this, loadedMapStyle)
                            .useDefaultLocationEngine(false)
                            .build();

            // Activate with the LocationComponentActivationOptions object
            locationComponent.activateLocationComponent(locationComponentActivationOptions);

            // Enable to make component visible
            locationComponent.setLocationComponentEnabled(true);

            // Set the component's camera mode
            locationComponent.setCameraMode(CameraMode.TRACKING);

            // Set the component's render mode
            locationComponent.setRenderMode(RenderMode.COMPASS);

            initLocationEngine();
        } else {
            permissionsManager = new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(this);
        }
    }

    /**
     * Set up the LocationEngine and the parameters for querying the device's location
     */
    @SuppressLint("MissingPermission")
    private void initLocationEngine() {
        locationEngine = LocationEngineProvider.getBestLocationEngine(this);

        LocationEngineRequest request = new LocationEngineRequest.Builder(DEFAULT_INTERVAL_IN_MILLISECONDS)
                .setPriority(LocationEngineRequest.PRIORITY_HIGH_ACCURACY)
                .setMaxWaitTime(DEFAULT_MAX_WAIT_TIME).build();

        locationEngine.requestLocationUpdates(request, callback, getMainLooper());
        locationEngine.getLastLocation(callback);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {
        Toast.makeText(this, "Veuillez donner votre autorisation de localisation", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPermissionResult(boolean granted) {
        if (granted) {
            if (mapboxMap.getStyle() != null) {
                enableLocationComponent(mapboxMap.getStyle());
            }
        } else {
            Toast.makeText(this, "Permission non accordée", Toast.LENGTH_LONG).show();
            finish();
        }
    }

    @Override
    public boolean onMapClick(@NonNull LatLng point) {
        PointF screenPoint = mapboxMap.getProjection().toScreenLocation(point);
        List<Feature> features = mapboxMap.queryRenderedFeatures(screenPoint, CALLOUT_LAYER_ID);
        if (!features.isEmpty()) {
            // we received a click event on the callout layer
            /*Feature feature = features.get(0);
            PointF symbolScreenPoint = mapboxMap.getProjection().toScreenLocation(convertToLatLng(feature));
            handleClickCallout(feature, screenPoint, symbolScreenPoint);*/
        } else {
            // we didn't find a click event on callout layer, try clicking maki layer
            return handleClickIcon(screenPoint);
        }
        return true;
    }

    public void setupData(final FeatureCollection collection) {
        if (mapboxMap == null) {
            return;
        }
        featureCollection = collection;
        mapboxMap.getStyle(new Style.OnStyleLoaded() {
            @Override
            public void onStyleLoaded(@NonNull Style style) {
                //etape 5
                setupSource(style);
                setupMarkerIconLayer(style);
                setupLoadingLayer(style);
                setupCalloutLayer(style);
                setupRecyclerView();
                hideLabelLayers(style);
            }
        });
    }

    //etape 6
    //mise en place des points
    private void setupSource(@NonNull Style loadedMapStyle) {
        source = new GeoJsonSource(SOURCE_ID, featureCollection);
        loadedMapStyle.addSource(source);
    }

    private void refreshSource() {
        if (source != null && featureCollection != null) {
            source.setGeoJson(featureCollection);
        }
    }

    //etape 6

    /**
     * Setup a layer with marker icon
     */
    private void setupMarkerIconLayer(@NonNull Style loadedMapStyle) {
        loadedMapStyle.addImage(ICON_ID, BitmapFactory.decodeResource(
                MapActivity.this.getResources(), R.drawable.mapbox_marker_icon_default));

        loadedMapStyle.addLayer(new SymbolLayer(LAYER_ID, SOURCE_ID)
                .withProperties(
                        iconImage(ICON_ID),

                        /* allows show all icons */
                        iconAllowOverlap(true),


                        /* when feature is in selected state, grow icon */
                        iconSize(match(Expression.toString(get(PROPERTY_SELECTED)), literal(1.0f),
                                stop("true", 1.7f))))
        );
    }

    /**
     * Setup layer indicating that there is an ongoing progress.
     */
    private void setupLoadingLayer(@NonNull Style loadedMapStyle) {
        loadedMapStyle.addLayerBelow(new CircleLayer(LOADING_LAYER_ID, SOURCE_ID)
                .withProperties(
                        circleRadius(interpolate(exponential(1), get(PROPERTY_LOADING_PROGRESS), getLoadingAnimationStops())),
                        circleColor(Color.GRAY),
                        circleOpacity(0.6f)
                )
                .withFilter(eq(get(PROPERTY_LOADING), literal(true))), LAYER_ID);
    }

    private Expression.Stop[] getLoadingAnimationStops() {
        List<Expression.Stop> stops = new ArrayList<>();
        for (int i = 0; i < LOADING_PROGRESS_STEPS; i++) {
            stops.add(stop(i, LOADING_CIRCLE_RADIUS * i / LOADING_PROGRESS_STEPS));
        }

        return stops.toArray(new Expression.Stop[LOADING_PROGRESS_STEPS]);
    }

    /**
     * Setup a layer with Android SDK call-outs
     * <p>
     * id of the feature is used as key for the iconImage
     * </p>
     */
    private void setupCalloutLayer(@NonNull Style loadedMapStyle) {
        loadedMapStyle.addLayer(new SymbolLayer(CALLOUT_LAYER_ID, SOURCE_ID)
                .withProperties(
                        /* show image with id  based on the value of the id feature property */
                        iconImage("{id}"),

                        /* set anchor of icon to bottom-left */
                        iconAnchor(Property.ICON_ANCHOR_BOTTOM),

                        /* offset icon slightly to match bubble layout */
                        iconOffset(new Float[]{+10.0f, -27.0f})
                )


                /* add a filter to show only when selected feature property is true */
                .withFilter(eq((get(PROPERTY_SELECTED)), literal(true))));
    }

    private void setupRecyclerView() {
        RecyclerView.Adapter adapter = new MapActivity.LocationRecyclerViewAdapter(this, featureCollection);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == SCROLL_STATE_IDLE) {
                    int index = layoutManager.findFirstVisibleItemPosition();
                    setSelected(index, false);
                }
            }
        });
        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);
    }

    private void hideLabelLayers(@NonNull Style style) {
        String id;
        for (Layer layer : style.getLayers()) {
            id = layer.getId();
            if (id.startsWith("place") || id.startsWith("poi") || id.startsWith("marine") || id.startsWith("road-label")) {
                layer.setProperties(visibility(Property.NONE));
            }
        }
    }

    /**
     * This method handles click events for callout symbols.
     * <p>
     * It creates a hit rectangle based on the the textView, offsets that rectangle to the location
     * of the symbol on screen and hit tests that with the screen point.
     * </p>
     *
     * @param feature           the feature that was clicked
     * @param screenPoint       the point on screen clicked
     * @param symbolScreenPoint the point of the symbol on screen
     */
   /* private void handleClickCallout(Feature feature, PointF screenPoint, PointF symbolScreenPoint) {

        String id = feature.getStringProperty(PROPERTY_ID);
        View view = viewMap.get(id);
        View textContainer = view.findViewById(R.id.text_container);

        // create hitbox for textView
        Rect hitRectText = new Rect();
        textContainer.getHitRect(hitRectText);

        // move hitbox to location of symbol
        hitRectText.offset((int) symbolScreenPoint.x, (int) symbolScreenPoint.y);

        // offset vertically to match anchor behaviour
        hitRectText.offset(0, -view.getMeasuredHeight());

        // hit test if clicked point is in textview hitbox
*//*        if (hitRectText.contains((int) screenPoint.x, (int) screenPoint.y)) {
            // user clicked on text
           *//**//* String callout = feature.getStringProperty("call-out");
            Toast.makeText(this, callout, Toast.LENGTH_LONG).show();*//**//*
        } else {
            // user clicked on icon
            List<Feature> featureList = featureCollection.features();
            for (int i = 0; i < featureList.size(); i++) {
                if (featureList.get(i).getStringProperty(PROPERTY_ID).equals(feature.getStringProperty(PROPERTY_ID))) {
                    //toggleFavourite(i);
                }
            }
        }*//*
    }*/

    /**
     * This method handles click events for marker symbols.
     * <p>
     * When a maki symbol is clicked, we moved that feature to the selected state.
     * </p>
     *
     * @param screenPoint the point on screen clicked
     */
    private boolean handleClickIcon(PointF screenPoint) {
        List<Feature> features = mapboxMap.queryRenderedFeatures(screenPoint, LAYER_ID);
        if (!features.isEmpty()) {

            String id = features.get(0).getStringProperty(PROPERTY_ID);

            List<Feature> featureList = featureCollection.features();

            for (int i = 0; i < featureList.size(); i++) {
                if (featureList.get(i).getStringProperty(PROPERTY_ID).equals(id)) {
                    setSelected(i, true);
                }
            }

            return true;
        }
        return false;
    }

    /**
     * Set a feature selected state with the ability to scroll the RecycleViewer to the provided index.
     *
     * @param index      the index of selected feature
     * @param withScroll indicates if the recyclerView position should be updated
     */
    private void setSelected(int index, boolean withScroll) {
        if (recyclerView.getVisibility() == View.GONE) {
            recyclerView.setVisibility(View.VISIBLE);
        }
        if (btnCancelNav.getVisibility() == View.VISIBLE) {
            btnCancelNav.setVisibility(View.GONE);
        }
        if (btnStartNav.getVisibility() == View.VISIBLE) {
            btnStartNav.setVisibility(View.GONE);
        }
        removeRoute();

        deselectAll(false);

        Feature feature = featureCollection.features().get(index);
        selectFeature(feature);
        animateCameraToSelection(feature);
        refreshSource();

        if (withScroll) {
            recyclerView.scrollToPosition(index);
        }
    }


    /**
     * Deselects the state of all the features
     */
    private void deselectAll(boolean hideRecycler) {
        for (Feature feature : featureCollection.features()) {
            feature.properties().addProperty(PROPERTY_SELECTED, false);
        }

        if (hideRecycler) {
            recyclerView.setVisibility(View.GONE);
        }
    }

    /**
     * Selects the state of a feature
     *
     * @param feature the feature to be selected.
     */
    private void selectFeature(Feature feature) {
        feature.properties().addProperty(PROPERTY_SELECTED, true);
    }

    private Feature getSelectedFeature() {
        if (featureCollection != null) {
            for (Feature feature : featureCollection.features()) {
                if (feature.getBooleanProperty(PROPERTY_SELECTED)) {
                    return feature;
                }
            }
        }

        return null;
    }

    /**
     * Animate camera to a feature.
     *
     * @param feature the feature to animate to
     */
    private void animateCameraToSelection(Feature feature, double newZoom) {
        CameraPosition cameraPosition = mapboxMap.getCameraPosition();

        if (animatorSet != null) {
            animatorSet.cancel();
        }

        animatorSet = new AnimatorSet();
        animatorSet.playTogether(
                createLatLngAnimator(cameraPosition.target, convertToLatLng(feature)),
                createZoomAnimator(cameraPosition.zoom, newZoom),
                createBearingAnimator(cameraPosition.bearing, feature.getNumberProperty("bearing").doubleValue()),
                createTiltAnimator(cameraPosition.tilt, feature.getNumberProperty("tilt").doubleValue())
        );
        animatorSet.start();
    }

    private void animateCameraToSelection(Feature feature) {
        double zoom = feature.getNumberProperty("zoom").doubleValue();
        animateCameraToSelection(feature, zoom);
    }

    /**
     * Set the favourite state of a feature based on the index.
     *
     * @param index the index of the feature to favourite/de-favourite
     */
/*    private void toggleFavourite(int index) {
        Feature feature = featureCollection.features().get(index);
        String title = feature.getStringProperty(PROPERTY_ID);
        boolean currentState = feature.getBooleanProperty(PROPERTY_FAVOURITE);
        feature.properties().addProperty(PROPERTY_FAVOURITE, !currentState);
        View view = viewMap.get(title);

        ImageView imageView = view.findViewById(R.id.logoView);
        imageView.setImageResource(currentState ? R.drawable.ic_favorite : R.drawable.ic_favorite_border);
        Bitmap bitmap = MapActivity.SymbolGenerator.generate(view);
        mapboxMap.getStyle(new Style.OnStyleLoaded() {
            @Override
            public void onStyleLoaded(@NonNull Style style) {
                style.addImage(title, bitmap);
                refreshSource();
            }
        });
    }*/

    /**
     * Invoked when the bitmaps have been generated from a view.
     */
    public void setImageGenResults(HashMap<String, View> viewMap, HashMap<String, Bitmap> imageMap) {
        mapboxMap.getStyle(new Style.OnStyleLoaded() {
            @Override
            public void onStyleLoaded(@NonNull Style style) {
                // calling addImages is faster as separate addImage calls for each bitmap.
                style.addImages(imageMap);
            }
        });
        // need to store reference to views to be able to use them as hitboxes for click events.
        MapActivity.this.viewMap = viewMap;
    }

    private void setActivityStep(@MapActivity.ActivityStep int activityStep) {
        Feature selectedFeature = getSelectedFeature();
        double zoom = stepZoomMap.get(activityStep);
        animateCameraToSelection(selectedFeature, zoom);

        currentStep = activityStep;
    }

    private LatLng convertToLatLng(Feature feature) {
        Point symbolPoint = (Point) feature.geometry();
        return new LatLng(symbolPoint.latitude(), symbolPoint.longitude());
    }

    private Animator createLatLngAnimator(LatLng currentPosition, LatLng targetPosition) {
        ValueAnimator latLngAnimator = ValueAnimator.ofObject(new MapActivity.LatLngEvaluator(), currentPosition, targetPosition);
        latLngAnimator.setDuration(CAMERA_ANIMATION_TIME);
        latLngAnimator.setInterpolator(new FastOutSlowInInterpolator());
        latLngAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mapboxMap.moveCamera(CameraUpdateFactory.newLatLng((LatLng) animation.getAnimatedValue()));
            }
        });
        return latLngAnimator;
    }

    private Animator createZoomAnimator(double currentZoom, double targetZoom) {
        ValueAnimator zoomAnimator = ValueAnimator.ofFloat((float) currentZoom, (float) targetZoom);
        zoomAnimator.setDuration(CAMERA_ANIMATION_TIME);
        zoomAnimator.setInterpolator(new FastOutSlowInInterpolator());
        zoomAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mapboxMap.moveCamera(CameraUpdateFactory.zoomTo((Float) animation.getAnimatedValue()));
            }
        });
        return zoomAnimator;
    }

    private Animator createBearingAnimator(double currentBearing, double targetBearing) {
        ValueAnimator bearingAnimator = ValueAnimator.ofFloat((float) currentBearing, (float) targetBearing);
        bearingAnimator.setDuration(CAMERA_ANIMATION_TIME);
        bearingAnimator.setInterpolator(new FastOutSlowInInterpolator());
        bearingAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mapboxMap.moveCamera(CameraUpdateFactory.bearingTo((Float) animation.getAnimatedValue()));
            }
        });
        return bearingAnimator;
    }

    private Animator createTiltAnimator(double currentTilt, double targetTilt) {
        ValueAnimator tiltAnimator = ValueAnimator.ofFloat((float) currentTilt, (float) targetTilt);
        tiltAnimator.setDuration(CAMERA_ANIMATION_TIME);
        tiltAnimator.setInterpolator(new FastOutSlowInInterpolator());
        tiltAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mapboxMap.moveCamera(CameraUpdateFactory.tiltTo((Float) animation.getAnimatedValue()));
            }
        });
        return tiltAnimator;
    }

    public void navigationItineraireBtnClick(){

        recyclerView.setVisibility(View.GONE);

        Point originPoint = Point.fromLngLat(locationComponent.getLastKnownLocation().getLongitude()
                ,locationComponent.getLastKnownLocation().getLatitude());
        Point destinationPoint = (Point) getSelectedFeature().geometry();
        System.out.println("originPoint : "+ originPoint);
        System.out.println("destinationPoint : "+ destinationPoint);
        getRoute(originPoint,destinationPoint);

        btnCancelNav.setVisibility(View.VISIBLE);
        btnStartNav.setVisibility(View.VISIBLE);
    }

    private void getRoute(Point originPoint, Point destinationPoint) {
        NavigationRoute.builder(this)
                .accessToken(getString(R.string.access_token))
                .origin(originPoint)
                .destination(destinationPoint)
                .build()
                .getRoute(new Callback<DirectionsResponse>() {
                    @Override
                    public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {
                        if (response.isSuccessful() && response.body() != null && !response.body().routes().isEmpty()){
                            currentRoute = response.body().routes().get(0);
                            if (navigationMapRoute!=null){
                                navigationMapRoute.updateRouteVisibilityTo(false);
                                navigationMapRoute.updateRouteArrowVisibilityTo(false);
                            }else {
                                navigationMapRoute = new NavigationMapRoute(null,
                                        mapView,mapboxMap,R.style.NavigationMapRoute);
                            }
                            navigationMapRoute.addRoute(currentRoute);
                        }

                    }

                    @Override
                    public void onFailure(Call<DirectionsResponse> call, Throwable t) {
                        System.out.println("Erreur: "+t.getMessage());
                    }
                });
    }

    //Open map and start navigation
    private void startNavigation(){
        boolean simulateRoute = true;
        // Create a NavigationLauncherOptions object to package everything together
        NavigationLauncherOptions options = NavigationLauncherOptions.builder()
                .directionsRoute(currentRoute)
                .shouldSimulateRoute(simulateRoute)
                .build();
        // Call this method with Context from within an Activity
        NavigationLauncher.startNavigation(this, options);
    }

    private void backToMiniDetatilView(){
        removeRoute();
        btnCancelNav.setVisibility(View.GONE);
        btnStartNav.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
    }

    private void removeRoute(){
        if (navigationMapRoute!=null) {
            navigationMapRoute.updateRouteVisibilityTo(false);
            navigationMapRoute.updateRouteArrowVisibilityTo(false);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
        if (navigationMapRoute != null) {
            navigationMapRoute.onStop();
        }
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        if (mapboxMap != null) {
            mapboxMap.removeOnMapClickListener(this);
        }
        mapView.onDestroy();
        // Prevent leaks
        if (locationEngine != null) {
            locationEngine.removeLocationUpdates(callback);
        }
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if (currentStep == STEP_LOADING || currentStep == STEP_READY) {
            setActivityStep(STEP_INITIAL);
            deselectAll(true);
            refreshSource();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    /**
     * CLASSE MapActivityLocationCallback
     */
    private static class MapActivityLocationCallback implements LocationEngineCallback<LocationEngineResult> {

        private final WeakReference<MapActivity> activityWeakReference;

        MapActivityLocationCallback(MapActivity activity) {
            this.activityWeakReference = new WeakReference<>(activity);
        }

        /**
         * The LocationEngineCallback interface's method which fires when the device's location has changed.
         *
         * @param result the LocationEngineResult object which has the last known location within it.
         */
        @Override
        public void onSuccess(LocationEngineResult result) {
            MapActivity activity = activityWeakReference.get();

            if (activity != null) {
                Location location = result.getLastLocation();

                if (location == null) {
                    return;
                }

                // Pass the new location to the Maps SDK's LocationComponent
                if (activity.mapboxMap != null && result.getLastLocation() != null) {
                    activity.mapboxMap.getLocationComponent().forceLocationUpdate(result.getLastLocation());
                }
            }
        }

        /**
         * The LocationEngineCallback interface's method which fires when the device's location can not be captured
         *
         * @param exception the exception message
         */
        @Override
        public void onFailure(@NonNull Exception exception) {
            Log.d("LocationChangeActivity", "message erreur :" + exception.getLocalizedMessage());
            MapActivity activity = activityWeakReference.get();
            if (activity != null) {
                Toast.makeText(activity, exception.getLocalizedMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Helper class to evaluate LatLng objects with a ValueAnimator
     */
    private static class LatLngEvaluator implements TypeEvaluator<LatLng> {

        private final LatLng latLng = new LatLng();

        @Override
        public LatLng evaluate(float fraction, LatLng startValue, LatLng endValue) {
            latLng.setLatitude(startValue.getLatitude()
                    + ((endValue.getLatitude() - startValue.getLatitude()) * fraction));
            latLng.setLongitude(startValue.getLongitude()
                    + ((endValue.getLongitude() - startValue.getLongitude()) * fraction));
            return latLng;
        }
    }

    /**
     * AsyncTask to load data from the Api.
     */
    private static class LoadPoiDataTask extends AsyncTask<Void, Void, FeatureCollection> {

        private static String gsonString = "";
        private final WeakReference<MapActivity> activityRef;

        LoadPoiDataTask(MapActivity activity) {
            this.activityRef = new WeakReference<>(activity);
        }

        @Override
        protected FeatureCollection doInBackground(Void... params) {
            MapActivity activity = activityRef.get();

            if (activity == null) {
                return null;
            }

            String geoJson = loadGeoJsonFromApi();
            return FeatureCollection.fromJson(geoJson);
        }

        //etape 3
        static String loadGeoJsonFromApi() {

            Call<List<Spectacle>> callSync = spectacleService.getAllSpectacles();

            try {
                retrofit2.Response<List<Spectacle>> response = callSync.execute();
                List<Spectacle> spectacles = response.body();

                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                gsonString = gson.toJson(SpectacleConverter.convertToSpectacleFeature(spectacles));
            } catch (Exception e) {
                System.out.println("@Error getAllSpectacles:" + e);
            }

            return gsonString;
        }

        @Override
        protected void onPostExecute(FeatureCollection featureCollection) {
            super.onPostExecute(featureCollection);
            MapActivity activity = activityRef.get();
            if (featureCollection == null || activity == null) {
                return;
            }
            //etape 4
            activity.setupData(featureCollection);
            new MapActivity.GenerateViewIconTask(activity).execute(featureCollection);
        }

    }

    /**
     * AsyncTask to generate Bitmap from Views to be used as iconImage in a SymbolLayer.
     * <p>
     * Call be optionally be called to update the underlying data source after execution.
     * </p>
     * <p>
     * Generating Views on background thread since we are not going to be adding them to the view hierarchy.
     * </p>
     */
    private static class GenerateViewIconTask extends AsyncTask<FeatureCollection, Void, HashMap<String, Bitmap>> {

        private final HashMap<String, View> viewMap = new HashMap<>();
        private final WeakReference<MapActivity> activityRef;
        private final boolean refreshSource;

        GenerateViewIconTask(MapActivity activity, boolean refreshSource) {
            this.activityRef = new WeakReference<>(activity);
            this.refreshSource = refreshSource;
        }

        GenerateViewIconTask(MapActivity activity) {
            this(activity, false);
        }

        @SuppressWarnings("WrongThread")
        @Override
        protected HashMap<String, Bitmap> doInBackground(FeatureCollection... params) {
            MapActivity activity = activityRef.get();
            if (activity != null) {
                HashMap<String, Bitmap> imagesMap = new HashMap<>();
                LayoutInflater inflater = LayoutInflater.from(activity);
                FeatureCollection featureCollection = params[0];

                for (Feature feature : featureCollection.features()) {
                    View view = inflater.inflate(R.layout.map_layer_bubble_on_marker, null);

                    String name = feature.getStringProperty(PROPERTY_ID);

/*                    //compter le nombre de spectacle pour une adresse
                    int cpt = 0;
                    Geometry position = feature.geometry();
                    for (int i = 0; i < featureCollection.features().size(); i++) {
                        if (featureCollection.features().get(i).geometry().equals(position)) {
                            cpt++;
                        }
                    }
                    TextView titleTv = view.findViewById(R.id.cpt);
                    titleTv.setText(String.valueOf(cpt));*/

                    String adresse = feature.getStringProperty("adresse");
                    TextView styleTv = view.findViewById(R.id.adresse);
                    styleTv.setText(adresse);

                   /* boolean favourite = feature.getBooleanProperty(PROPERTY_FAVOURITE);
                    ImageView imageView = view.findViewById(R.id.logoView);
                    imageView.setImageResource(favourite ? R.drawable.ic_favorite : R.drawable.ic_favorite_border);*/

                    Bitmap bitmap = MapActivity.SymbolGenerator.generate(view);
                    imagesMap.put(name, bitmap);
                    viewMap.put(name, view);
                }

                return imagesMap;
            } else {
                return null;
            }
        }

        @Override
        protected void onPostExecute(HashMap<String, Bitmap> bitmapHashMap) {
            super.onPostExecute(bitmapHashMap);
            MapActivity activity = activityRef.get();
            if (activity != null && bitmapHashMap != null) {

                activity.setImageGenResults(viewMap, bitmapHashMap);
                if (refreshSource) {
                    activity.refreshSource();
                }
            }
        }
    }

    /**
     * Utility class to generate Bitmaps for Symbol.
     */
    private static class SymbolGenerator {

        /**
         * Generate a Bitmap from an Android SDK View.
         *
         * @param view the View to be drawn to a Bitmap
         * @return the generated bitmap
         */
        static Bitmap generate(@NonNull View view) {
            int measureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
            view.measure(measureSpec, measureSpec);

            int measuredWidth = view.getMeasuredWidth();
            int measuredHeight = view.getMeasuredHeight();

            view.layout(0, 0, measuredWidth, measuredHeight);
            Bitmap bitmap = Bitmap.createBitmap(measuredWidth, measuredHeight, Bitmap.Config.ARGB_8888);
            bitmap.eraseColor(Color.TRANSPARENT);
            Canvas canvas = new Canvas(bitmap);
            view.draw(canvas);
            return bitmap;
        }
    }

    /**
     * RecyclerViewAdapter adapting features to cards.
     */
    static class LocationRecyclerViewAdapter extends
            RecyclerView.Adapter<MapActivity.LocationRecyclerViewAdapter.MyViewHolder> {

        private List<Feature> featureCollection;
        private MapActivity activity;

        LocationRecyclerViewAdapter(MapActivity activity, FeatureCollection featureCollection) {
            this.activity = activity;
            this.featureCollection = featureCollection.features();
        }

        @Override
        public MapActivity.LocationRecyclerViewAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.map_layer_mini_detail, parent, false);
            return new MapActivity.LocationRecyclerViewAdapter.MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MapActivity.LocationRecyclerViewAdapter.MyViewHolder holder, int position) {
            Feature feature = featureCollection.get(position);

            JsonArray images = feature.getProperty("photosUrl").getAsJsonArray();
            String imageUri = ServiceGenerator.API_IMAGE + images.get(0).getAsString();

            Picasso.get().load(imageUri).into(holder.image);
            holder.titre.setText(feature.getStringProperty("titre"));
            holder.type.setText(feature.getStringProperty("typeSpectacle"));

            String dateHeure = feature.getStringProperty("dateHeure");
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy hh:mm");
            String parseDateHeure = format.format(dateHeure);
            String[] newDateHeure = parseDateHeure.toString().split(" ");
            holder.date.setText(newDateHeure[0]);
            holder.heure.setText(newDateHeure[1]);

            String prix = feature.getStringProperty("prix");
            if (prix.equals("0")) {
                prix = "Gratuit";
            } else {
                prix = prix + " €";
            }
            holder.prix.setText(prix);
            holder.btnDetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(activity, DetailActivity.class);
                    intent.putExtra("spectacleId", feature.getStringProperty("id"));
                    activity.startActivity(intent);
                }
            });
            holder.btnItineraire.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    activity.navigationItineraireBtnClick();
                }
            });
            holder.closebtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    activity.deselectAll(true);
                    activity.refreshSource();
                }
            });
/*            holder.setClickListener(new MapActivity.ItemClickListener() {
                @Override
                public void onClick(View view, int position) {
                    if (activity != null) {
                        activity.toggleFavourite(position);
                    }
                }
            });*/
        }

        @Override
        public int getItemCount() {
            return featureCollection.size();
        }

        /**
         * ViewHolder for RecyclerView.
         * Modele pour chaque cellule
         */
        static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            TextView closebtn;
            ImageView image;
            TextView titre;
            TextView type;
            TextView date;
            TextView heure;
            TextView prix;
            Button btnDetail;
            Button btnItineraire;

            CardView singleCard;
            MapActivity.ItemClickListener clickListener;

            MyViewHolder(View view) {
                super(view);
                closebtn = view.findViewById(R.id.cardviewClose);
                image = view.findViewById(R.id.cardviewImage);
                titre = view.findViewById(R.id.cardviewTitre);
                type = view.findViewById(R.id.cardviewType);
                date = view.findViewById(R.id.cardviewDate);
                heure = view.findViewById(R.id.cardviewHeure);
                prix = view.findViewById(R.id.cardviewPrix);
                btnDetail = view.findViewById(R.id.cardviewBtnDetail);
                btnItineraire = view.findViewById(R.id.cardviewBtnItineraire);

                singleCard = view.findViewById(R.id.single_location_cardview);
                singleCard.setOnClickListener(this);
            }

            void setClickListener(MapActivity.ItemClickListener itemClickListener) {
                this.clickListener = itemClickListener;
            }

            @Override
            public void onClick(View view) {
                clickListener.onClick(view, getLayoutPosition());
            }
        }
    }

    interface ItemClickListener {
        void onClick(View view, int position);
    }

}