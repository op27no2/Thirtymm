package op27no2.fitness.Centurion2.fragments.run;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rilixtech.materialfancybutton.MaterialFancyButton;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;

import op27no2.fitness.Centurion2.Database.AppDatabase;
import op27no2.fitness.Centurion2.R;
import op27no2.fitness.Centurion2.RecyclerItemClickListener;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;
import static com.mapbox.mapboxsdk.Mapbox.getApplicationContext;


public class RunSettings extends Fragment {
    public Context mContext;
    private RecyclerView mRecyclerView;
    public LinearLayoutManager mLayoutManager;
    private RunTypeAdapter mAdapter;
    private RecyclerView mRecyclerView2;
    public LinearLayoutManager mLayoutManager2;
    private RunTypeAdapter mAdapter2;
    private ArrayList<String> activeTypes = new ArrayList<String>();
    private ArrayList<String> proposedTypes = new ArrayList<String>();
    private SharedPreferences prefs;
    private SharedPreferences.Editor edt;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_runsettings, container, false);
        mContext = getActivity();
        prefs = getActivity().getSharedPreferences("PREFS", Context.MODE_PRIVATE);
        edt = getActivity().getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit();

        activeTypes.add("Run");
        activeTypes.add("Walking");
        activeTypes.add("Rowing");

        proposedTypes.add("Cycling");
        proposedTypes.add("Kayaking");
        proposedTypes.add("Skiing");
        proposedTypes.add("Hiking");
        proposedTypes.add("Tabata");

        mLayoutManager = new LinearLayoutManager(mContext);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mAdapter = new RunTypeAdapter(activeTypes);
        mRecyclerView = view.findViewById(R.id.my_recycler_view_active);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(mContext, mRecyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        System.out.println("segment item clicked: "+position);
                        mAdapter.setSelected(position);
                        mAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onItemLongClick(View view, int position) {
                        System.out.println("segment item long clicked: "+position);

                    }
                })
        );

        mLayoutManager2 = new LinearLayoutManager(mContext);
        mLayoutManager2.setOrientation(LinearLayoutManager.VERTICAL);
        mAdapter2 = new RunTypeAdapter(activeTypes);
        mRecyclerView2 = view.findViewById(R.id.my_recycler_view_options);
        mRecyclerView2.setHasFixedSize(true);
        mRecyclerView2.setLayoutManager(mLayoutManager2);
        mRecyclerView2.setNestedScrollingEnabled(false);
        mRecyclerView2.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView2.setAdapter(mAdapter2);
        mRecyclerView2.addOnItemTouchListener(
                new RecyclerItemClickListener(mContext, mRecyclerView2, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        System.out.println("segment item clicked: "+position);
                        mAdapter2.setSelected(position);
                        mAdapter2.notifyDataSetChanged();
                    }

                    @Override
                    public void onItemLongClick(View view, int position) {
                        System.out.println("segment item long clicked: "+position);

                    }
                })
        );

        MaterialFancyButton backupDatabase = view.findViewById(R.id.backup_database);
        backupDatabase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backupDatabase(mContext);
            }
        });

        MaterialFancyButton restoreDatabase = view.findViewById(R.id.restore_database);
        restoreDatabase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                restoreDBIntent();
            }
        });






        return view;
    }




    public void backupDatabase(Context context) {
        AppDatabase appDatabase = AppDatabase.getAppDatabase(context);
     //   appDatabase.close();
        File dbfile = context.getDatabasePath("user-database");

        String path = getFilePath("backup"+System.currentTimeMillis());
        System.out.println("path "+path);
        File savefile = new File(path);

        if (savefile.exists()) {
            System.out.println("File exists. Deleting it and then creating new file.");
            savefile.delete();
        }

        try {
            if (savefile.createNewFile()) {
                System.out.println("create new file");

                int buffersize = 8 * 1024;
                byte[] buffer = new byte[buffersize];
                int bytes_read = buffersize;
                OutputStream savedb = new FileOutputStream(path);
                InputStream indb = new FileInputStream(dbfile);
                while ((bytes_read = indb.read(buffer, 0, buffersize)) > 0) {
                    savedb.write(buffer, 0, bytes_read);
                }
                savedb.flush();
                indb.close();
                savedb.close();
                SharedPreferences sharedPreferences = context.getSharedPreferences("PREFS", MODE_PRIVATE);
                sharedPreferences.edit().putString("backupFileName", path).apply();
             //TODO add this back
              //  updateLastBackupTime(sharedPreferences);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println( "export error: " + e);
        }
    }


    public String getFilePath(String name) {
        //   final String directory = Environment.getExternalStorageDirectory() + File.separator + "ParentScope";
        String directory = mContext.getExternalFilesDir(null).getAbsolutePath()+ File.separator + "LREBackups";

        if (!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            Toast.makeText(getActivity(), "Failed to get External Storage", Toast.LENGTH_SHORT).show();
            return null;
        }
        final File folder = new File(directory);
        boolean success = true;
        if (!folder.exists()) {
            success = folder.mkdir();
        }
        String filePath;
        if (success) {
            String fileName = name;
            filePath = directory + File.separator + fileName;

            System.out.println("set file path: "+filePath);

        } else {
            Toast.makeText(mContext, "Failed to create Recordings directory", Toast.LENGTH_SHORT).show();
            System.out.println("Failed to create Recordings directory");
            return null;
        }

        return filePath;
    }


    private void restoreDBIntent() {
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.setType("*/*");
        startActivityForResult(Intent.createChooser(i, "Select DB File"), 12);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        System.out.println("fragment on Resume");

        if (requestCode == 12 && resultCode == RESULT_OK && data != null) {
            Uri fileUri = data.getData();
            try {
                assert fileUri != null;
                InputStream inputStream = getActivity().getContentResolver().openInputStream(fileUri);
                if (validFile(fileUri)) {
                    restoreDatabase(inputStream);
                } else {
                    System.out.println("restore failed");
                }
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private boolean validFile(Uri fileUri) {
        ContentResolver cr = getActivity().getContentResolver();
        String mime = cr.getType(fileUri);
        return "application/octet-stream".equals(mime);
    }
    private void restoreDatabase(InputStream inputStreamNewDB) {
        AppDatabase appDatabase = AppDatabase.getAppDatabase(mContext);
        appDatabase.close();

        //Delete the existing restoreFile and create a new one.
        prefs.edit().putBoolean("restoringDatabase", true).apply();

        deleteRestoreBackupFile(getApplicationContext());
        backupDatabaseForRestore(getApplicationContext());

        File oldDB = mContext.getDatabasePath("user-database");
        if (inputStreamNewDB != null) {
            try {
                copyFile((FileInputStream) inputStreamNewDB, new FileOutputStream(oldDB));
                System.out.println("database restored?");

                //Take the user to home screen and there we will validate if the database file was actually restored correctly.
            } catch (IOException e) {
                System.out.println("restore fail: "+e.getMessage());
                e.printStackTrace();
            }
        } else {
            System.out.println("restore file does not exist");
        }
    }



    public void copyFile(FileInputStream fromFile, FileOutputStream toFile) throws IOException {
        FileChannel fromChannel = null;
        FileChannel toChannel = null;
        try {
            fromChannel = fromFile.getChannel();
            toChannel = toFile.getChannel();
            fromChannel.transferTo(0, fromChannel.size(), toChannel);
        } finally {
            try {
                if (fromChannel != null) {
                    fromChannel.close();
                }
            } finally {
                if (toChannel != null) {
                    toChannel.close();
                }
            }
        }
    }


    public void backupDatabaseForRestore(Context context) {
        File dbfile = context.getDatabasePath("user-database");
        String savePath = getFilePath("restore_backup");
        File savefile = new File(savePath);

        if (savefile.exists()) {
            System.out.println( "Backup Restore - File exists. Deleting it and then creating new file.");
            savefile.delete();
        }
        try {
            if (savefile.createNewFile()) {
                int buffersize = 8 * 1024;
                byte[] buffer = new byte[buffersize];
                int bytes_read = buffersize;
                OutputStream savedb = new FileOutputStream(savePath);
                InputStream indb = new FileInputStream(dbfile);
                while ((bytes_read = indb.read(buffer, 0, buffersize)) > 0) {
                    savedb.write(buffer, 0, bytes_read);
                }
                savedb.flush();
                indb.close();
                savedb.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("ex for restore file: " + e.getMessage());
        }
    }

    public void deleteRestoreBackupFile(Context context) {
        File directory = new File(getFilePath("restore_backup"));

     //   String sfpath = directory.getPath() + File.separator + "restore_backup";
        //This is to prevent deleting extra file being deleted which is mentioned in previous comment lines.
      //  File restoreFile = new File(sfpath);

        if (directory.exists()) {
            directory.delete();
        }
    }


}

