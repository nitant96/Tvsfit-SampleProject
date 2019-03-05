package com.example.nitantsood.tvssampleapplication;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.Fade;
import android.util.Log;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.miguelcatalan.materialsearchview.MaterialSearchView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    EmployeeListAdapter employeeListAdapter;
    ArrayList<OneCustomerDetail> customerList;
    ArrayList<String> nameList=new ArrayList<>();
    MaterialSearchView searchView;
    ArrayList<OneCustomerDetail>  employeeSearchList=new ArrayList<>();
    ArrayList<CityData> cityDataArrayList=new ArrayList<>();
    HashMap<String,ArrayList<OneCustomerDetail>> cityMapping= new HashMap<>();
    String resultMainData;
    boolean isLocationFetchingDone=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent=getIntent();
        resultMainData=intent.getStringExtra("mainData");

        Toolbar toolbar = findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);


        FloatingActionButton fab = findViewById(R.id.fabMain);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isLocationFetchingDone) {
                    Intent intent = new Intent(MainActivity.this, MapsActivity.class);
                    intent.putExtra("cityList", cityDataArrayList);
                    intent.putExtra("empData", customerList);
                    intent.putExtra("mapData", cityMapping);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(MainActivity.this, "Loading the Cities, try again in few moments", Toast.LENGTH_SHORT).show();
                }
            }
        });



        customerList=new ArrayList<>();
        recyclerView=findViewById(R.id.recyclerView);
        searchView = (MaterialSearchView) findViewById(R.id.search_view);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        setAdapterToinitialState();
        getDataIntoList();
        mapCitiesFromData();
        getLocationFromList();

        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //Do some magic
                employeeSearchList.clear();
                if(!query.equals(""))
                findInListAndPopulate(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //Do some magic
                employeeSearchList.clear();
                if(!newText.equals(""))
                findInListAndPopulate(newText);
                return true;
            }
        });
        searchView.setSubmitOnClick(true);
        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
                //Do some magic
                employeeSearchList.clear();
//                Toast.makeText(MainActivity.this, "onSearchViewOpened Called", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSearchViewClosed() {
                //Do some magic
//                Toast.makeText(MainActivity.this, "onSearchViewClosed Called", Toast.LENGTH_SHORT).show();
                employeeSearchList.clear();
                setAdapterToinitialState();

            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();


    }

    //setting the search button for SearchView
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);

        MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);

        return true;
    }

    //Overriding Back press for SearchView
    @Override
    public void onBackPressed() {
        if (searchView.isSearchOpen()) {
            searchView.closeSearch();
        } else {
            super.onBackPressed();
        }
    }

    //Setting the RecyclerView to populate the Initial List
    private void setAdapterToinitialState() {
        employeeListAdapter=new EmployeeListAdapter(this, customerList, new EmployeeListAdapter.ItemClickListener() {
            @Override
            public void onEmployeeClicked(View view, int position) {
                Intent mainToDetailIntent=new Intent(getApplicationContext(),EmployeeDetailActivity.class);
                mainToDetailIntent.putExtra("clickedEmployeeDetail",customerList.get(position));
                Toast.makeText(getApplicationContext(),customerList.get(position).getName(), Toast.LENGTH_SHORT).show();
                Pair<View,String> p1=Pair.create(view.findViewById(R.id.empImg),"EmployeeImageToTransition");
                Pair<View,String> p2=Pair.create(view.findViewById(R.id.empName),"EmployeeNameToTransition");
                Pair<View,String> p3=Pair.create(view.findViewById(R.id.empOccupation),"EmployeeOccupationToTransition");
                Pair<View,String> p4=Pair.create(view.findViewById(R.id.empLoc),"EmployeeLocationToTransition");
                ActivityOptions activityOptions=ActivityOptions.makeSceneTransitionAnimation(MainActivity.this,p1,p2,p3,p4);
                startActivity(mainToDetailIntent,activityOptions.toBundle());
            }
        });
        recyclerView.setAdapter(employeeListAdapter);

    }

    //Setting the RecyclerView to populate the Search Query list
    private void findInListAndPopulate(String query) {
        for(int index=0;index<customerList.size();index++ ){
            if(customerList.get(index).getName().toLowerCase().contains(query.toLowerCase()) || customerList.get(index).getId().contains(query)){
                employeeSearchList.add(customerList.get(index));
            }
            employeeListAdapter=new EmployeeListAdapter(this, employeeSearchList, new EmployeeListAdapter.ItemClickListener() {
                @Override
                public void onEmployeeClicked(View view, int position) {
                        // Swap without transition
                        Intent mainToDetailIntent=new Intent(getApplicationContext(),EmployeeDetailActivity.class);
                        mainToDetailIntent.putExtra("clickedEmployeeDetail",employeeSearchList.get(position));
                        Toast.makeText(getApplicationContext(),employeeSearchList.get(position).getName(), Toast.LENGTH_SHORT).show();
                        Pair<View,String> p1=Pair.create(view.findViewById(R.id.empImg),"EmployeeImageToTransition");
                        Pair<View,String> p2=Pair.create(view.findViewById(R.id.empName),"EmployeeNameToTransition");
                        Pair<View,String> p3=Pair.create(view.findViewById(R.id.empOccupation),"EmployeeOccupationToTransition");
                        Pair<View,String> p4=Pair.create(view.findViewById(R.id.empLoc),"EmployeeLocationToTransition");
                        ActivityOptions activityOptions=ActivityOptions.makeSceneTransitionAnimation(MainActivity.this,p1,p2,p3,p4);
                        startActivity(mainToDetailIntent,activityOptions.toBundle());

                }
            });
            recyclerView.setAdapter(employeeListAdapter);
            employeeListAdapter.notifyDataSetChanged();
        }
    }

    //Method to get Data from URL to the ArrayList of employees
    private void getDataIntoList() {

        try {
            JSONObject reader = new JSONObject(resultMainData);
            String data=reader.getString("TABLE_DATA");
            JSONObject dataObj=new JSONObject(data);
            JSONArray mainData =dataObj.getJSONArray("data");
            int noOfEntries=mainData.length();
            for(int i=0;i<noOfEntries;i++){
                JSONArray oneItem=(JSONArray) mainData.get(i);

                OneCustomerDetail oneCustomerDetail=new OneCustomerDetail();
                oneCustomerDetail.setName(oneItem.get(0).toString());
                oneCustomerDetail.setOccupation(oneItem.get(1).toString());
                oneCustomerDetail.setLocation(oneItem.get(2).toString());
                oneCustomerDetail.setId(oneItem.get(3).toString());
                oneCustomerDetail.setDate(oneItem.get(4).toString());
                oneCustomerDetail.setPrice(oneItem.get(5).toString());

                customerList.add(oneCustomerDetail);
                nameList.add(oneItem.get(0).toString());
            }
            employeeListAdapter.notifyDataSetChanged();
            Object[] objNames = nameList.toArray();
            String[] strNames = Arrays. copyOf(objNames, objNames. length, String[].class);
//            searchView.setSuggestions(strNames);
            searchView.setAnimationDuration(50);
            searchView.setHint("Search with a Name");
            searchView.clearFocus();
//            searchView.showSearch();
//            searchView.closeSearch();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //Method to Map the Different Distinct cities
    private void mapCitiesFromData() {
        for(int index=0;index<customerList.size();index++){
            String currentCityName=customerList.get(index).getLocation();
            ArrayList<OneCustomerDetail> currentList;
            if(!cityMapping.containsKey(currentCityName)){
                currentList=new ArrayList<>();
            }
            else{
                currentList=cityMapping.get(currentCityName);
            }
            currentList.add(customerList.get(index));
            cityMapping.put(currentCityName,currentList);
        }
    }

    //Method to get the Location Coordinates of the given states/cities
    private void getLocationFromList() {
        String myUrl = "https://maps.googleapis.com/maps/api/geocode/json?address=";
        String result;
        for(String cityName:cityMapping.keySet()){
            HTTPMapGetRequest httpMapGetRequest=new HTTPMapGetRequest();
            try {
                result=httpMapGetRequest.execute(myUrl,cityName).get();
                Log.d("meriMarzi",result);
                JSONObject jsonObject = new JSONObject(result);

                double lng = ((JSONArray)jsonObject.get("results")).getJSONObject(0)
                        .getJSONArray("locations").getJSONObject(0).getJSONObject("latLng")
                        .getDouble("lng");
                double lat = ((JSONArray)jsonObject.get("results")).getJSONObject(0)
                        .getJSONArray("locations").getJSONObject(0).getJSONObject("latLng")
                        .getDouble("lat");

                CityData data=new CityData(cityName,lat,lng);
                cityDataArrayList.add(data);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            isLocationFetchingDone=true;
        }

    }
}
