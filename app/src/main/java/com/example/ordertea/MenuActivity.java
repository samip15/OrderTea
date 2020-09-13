package com.example.ordertea;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.test.espresso.IdlingResource;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.ordertea.Model.Tea;
import com.example.ordertea.idellingresource.SimpleIdlingResource;

import java.util.ArrayList;

public class MenuActivity extends AppCompatActivity implements ImageDownloader.DelayerCallback {

    public static final String EXTRA_TEA_NAME  = "com.example.ordertea.EXTRA_TEA_NAME";

    @Nullable
    private SimpleIdlingResource mIdlingResource;

    /**
     * Only called from tests, creates and returns SimpleIdlingResource
     */
    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource()
    {
        if(mIdlingResource == null)
        {
            mIdlingResource = new SimpleIdlingResource();
        }
        return mIdlingResource;
    }

    @Override
    protected void onStart() {
        super.onStart();
        ImageDownloader.downloadImage(this,MenuActivity.this,mIdlingResource);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        Toolbar menuToolbar = findViewById(R.id.menu_toolbar);
        setSupportActionBar(menuToolbar);
        getSupportActionBar().setTitle(getString(R.string.menu_title));

        //get the resource
        getIdlingResource();
    }

    @Override
    public void onDone(ArrayList<Tea> teas) {
        //populate using gridview
        GridView gridView = findViewById(R.id.tea_grid_view);
        TeaMenueAdapter adapter = new TeaMenueAdapter(this,R.layout.grid_item_layout,teas);
        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Tea item = (Tea) adapterView.getItemAtPosition(position);
                //intent to order activity
                Intent mTeaIntent = new Intent(MenuActivity.this,OrderActivity.class);
                String teaName = item.getTeaName();
                mTeaIntent.putExtra(EXTRA_TEA_NAME,teaName);
                startActivity(mTeaIntent);
            }
        });
    }
}
