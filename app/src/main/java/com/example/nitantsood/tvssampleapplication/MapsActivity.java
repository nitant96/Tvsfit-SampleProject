package com.example.nitantsood.tvssampleapplication;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.res.Resources;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.util.Pair;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    ArrayList<CityData> cityData=new ArrayList<>();
    ArrayList<OneCustomerDetail> empData=new ArrayList<>();
    HashMap<String,ArrayList<OneCustomerDetail>> cityMap=new HashMap<>();
    private BottomSheetBehavior mBottomSheetBehavior1;
    final int mScreenHeight= Resources.getSystem().getDisplayMetrics().heightPixels;

    View bottomSheet;
    RecyclerView recyclerView;
    TextView bottomSheetMainHeader;
    EmployeeListAdapter employeeListAdapter;
    ArrayList<Marker> geoMarker=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        bottomSheetMainHeader=findViewById(R.id.bottomSheetHeader);
        bottomSheet = findViewById(R.id.mapBottomSheet);
        recyclerView=findViewById(R.id.bottomSheetRecylerView);
        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        mBottomSheetBehavior1 = BottomSheetBehavior.from(bottomSheet);
        mBottomSheetBehavior1.setState(BottomSheetBehavior.STATE_EXPANDED);
//        mBottomSheetBehavior1.
//        mBottomSheetBehavior1.setPeekHeight(mScreenHeight/2);

        Intent intent=getIntent();
        cityData=(ArrayList<CityData>) intent.getSerializableExtra("cityList");
        empData=(ArrayList<OneCustomerDetail>) intent.getSerializableExtra("empData");
        cityMap=(HashMap<String,ArrayList<OneCustomerDetail>>) intent.getSerializableExtra("mapData");
        setAdapterOfBottomSheet(empData);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        for(int index=0;index<cityData.size();index++) {
            String currentCityName=cityData.get(index).getCityName();
            LatLng latLng = new LatLng(cityData.get(index).getLattitude(),cityData.get(index).getLongitude());
            Marker currentMarker=mMap.addMarker(new MarkerOptions().position(latLng).title(currentCityName));
            currentMarker.setTag(currentCityName);
            currentMarker.setTitle(cityMap.get(currentCityName).size()+" employees work here");
            currentMarker.showInfoWindow();
            ArrayList<OneCustomerDetail> currentList=cityMap.get(currentCityName);
            setAdapterOfBottomSheet(currentList);
            bottomSheetMainHeader.setText("Employees working at "+currentCityName+" ("+currentList.size()+")");
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        }

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                String cityName=(String) marker.getTag();
                marker.setTitle(cityMap.get(cityName).size()+" employees work here");
                marker.showInfoWindow();
                bottomSheetMainHeader.setText("Employees working at "+cityName+" ("+cityMap.get(cityName).size()+")");
                setAdapterOfBottomSheet(cityMap.get(cityName));
                return true;
            }
        });

    }

    private void setAdapterOfBottomSheet(final ArrayList<OneCustomerDetail> currentList) {
        employeeListAdapter=new EmployeeListAdapter(this, currentList, new EmployeeListAdapter.ItemClickListener() {
            @Override
            public void onEmployeeClicked(View view, int position) {
                Intent mainToDetailIntent=new Intent(getApplicationContext(),EmployeeDetailActivity.class);
                mainToDetailIntent.putExtra("clickedEmployeeDetail",currentList.get(position));
                Toast.makeText(getApplicationContext(),currentList.get(position).getName(), Toast.LENGTH_SHORT).show();
                Pair<View,String> p1=Pair.create(view.findViewById(R.id.empImg),"EmployeeImageToTransition");
                Pair<View,String> p2=Pair.create(view.findViewById(R.id.empName),"EmployeeNameToTransition");
                Pair<View,String> p3=Pair.create(view.findViewById(R.id.empOccupation),"EmployeeOccupationToTransition");
                Pair<View,String> p4=Pair.create(view.findViewById(R.id.empLoc),"EmployeeLocationToTransition");
                ActivityOptions activityOptions=ActivityOptions.makeSceneTransitionAnimation(MapsActivity.this,p1,p2,p3,p4);
                startActivity(mainToDetailIntent,activityOptions.toBundle());
            }
        });
        recyclerView.setAdapter(employeeListAdapter);
    }
}
