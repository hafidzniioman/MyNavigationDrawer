package com.example.hafidzniioman.mynavigationdrawer;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.text.TextUtils;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, LoaderManager.LoaderCallbacks<ArrayList<MovieItems>>
{
    ListView listView;
    MovieAdapter adapter;
    EditText edtMovieName;
    Button btnSearch;
    static final String EXTRAS_MOVIE = "EXTRAS_MOVIE";
    CircleImageView profileCircleImageView;
    String profileImageUrl = "https://media.licdn.com/mpr/mpr/shrinknp_400_400/AAEAAQAAAAAAAAb8AAAAJGVlMmE5ZmNiLTZlMDQtNDcyMi04OWUzLTcwYWIxZTMzYjhmZA.jpg";


    DrawerLayout drawer;
    Toolbar toolbar;
    ActionBarDrawerToggle toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        adapter = new MovieAdapter(this);
        adapter.notifyDataSetChanged();
        listView = (ListView) findViewById(R.id.lv_movie);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MovieItems movieItems = (MovieItems) parent.getItemAtPosition(position);

                Intent intent = new Intent(MainActivity.this, MovieDetail.class);

                intent.putExtra(MovieDetail.EXTRA_TITLE, movieItems.getMovieName());
                intent.putExtra(MovieDetail.EXTRA_DESC, movieItems.getMovieDescription());
                intent.putExtra(MovieDetail.EXTRA_POSTER, movieItems.getmoviePoster());
                intent.putExtra(MovieDetail.EXTRA_DATE, movieItems.getMovieDate());

                startActivity(intent);
            }
        });

        edtMovieName = (EditText) findViewById(R.id.edt_search_movie);
        btnSearch = (Button) findViewById(R.id.btn_search_movie);
        btnSearch.setOnClickListener(movieListener);

        String movie = edtMovieName.getText().toString();
        Bundle bundle = new Bundle();
        bundle.putString(EXTRAS_MOVIE, movie);

        getSupportLoaderManager().initLoader(0, bundle, this);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Home");
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Toast.makeText(MainActivity.this, "Halo ini action dari snackbar", Toast.LENGTH_SHORT).show();
                            }
                        }).show();
            }
        });
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            profileCircleImageView = (CircleImageView) navigationView.getHeaderView(0).findViewById(R.id.imageView);
        Glide.with(MainActivity.this)
                .load(profileImageUrl)
                .into(profileCircleImageView);
        navigationView.setNavigationItemSelectedListener(this);

        if (savedInstanceState == null){
            Fragment currentFragment = new HomeFragment();
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.content_main,currentFragment)
                    .commit();
        }
    }

    @NonNull
    @Override
    public Loader<ArrayList<MovieItems>> onCreateLoader(int id, @Nullable Bundle args) {
        String movieCollection = "";
        if (args != null) {
            movieCollection = args.getString(EXTRAS_MOVIE);
        }
        return new MovieLoader(this, movieCollection);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<ArrayList<MovieItems>> loader, ArrayList<MovieItems> data) {
        adapter.setData(data);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<ArrayList<MovieItems>> loader) {
        adapter.setData(null);
    }

    View.OnClickListener movieListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String movie = edtMovieName.getText().toString();
            if (TextUtils.isEmpty(movie))
                return;

            Bundle bundle = new Bundle();
            bundle.putString(EXTRAS_MOVIE, movie);
            getSupportLoaderManager().restartLoader(0, bundle, MainActivity.this);
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        Bundle bundle = new Bundle();

        Fragment fragment = null;

        String title = "";
        if (id == R.id.nav_home){

            title = "Home";
            fragment = new HomeFragment();

        } else if (id == R.id.nav_camera) {

            title = "Camera";
            fragment = new HalamanFragment();
            bundle.putString(HalamanFragment.EXTRAS,"Camera");
            fragment.setArguments(bundle);

        } else if (id == R.id.nav_gallery) {

            title = "Gallery";
            fragment = new HalamanFragment();
            bundle.putString(HalamanFragment.EXTRAS,"Gallery");
            fragment.setArguments(bundle);

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.content_main, fragment)
                    .commit();
        }

        getSupportActionBar().setTitle(title);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
