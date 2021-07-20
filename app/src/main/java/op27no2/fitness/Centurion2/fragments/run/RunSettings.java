package op27no2.fitness.Centurion2.fragments.run;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
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
import op27no2.fitness.Centurion2.Database.Repository;
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
    private SharedPreferences prefs;
    private SharedPreferences.Editor edt;
    private Repository mRepository;
    private int selected = 1000;
    private int selectedList = 3;

    private  ArrayList<RunType> allRunTypes = new ArrayList<RunType>();
    private  ArrayList<RunType> listedRunTypes = new ArrayList<RunType>();
    private  ArrayList<RunType> storedRunTypes = new ArrayList<RunType>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_runsettings, container, false);
        mContext = getActivity();
        prefs = getActivity().getSharedPreferences("PREFS", Context.MODE_PRIVATE);
        edt = getActivity().getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit();
        mRepository = new Repository(getActivity());

        EditText mNameEdit = view.findViewById(R.id.edit_name);
        Switch mSwitch = view.findViewById(R.id.list_switch);
        Spinner mSpinnerDistance = view.findViewById(R.id.spinner_distance);
        Spinner mSpinnerPace = view.findViewById(R.id.spinner_pace);

        EditText mCalFactorEdit = view.findViewById(R.id.cal_factor);
        Spinner mCalUnits = view.findViewById(R.id.cal_units);

     /*   if(prefs.getBoolean("setup_run_type", true) == true) {
            System.out.println("first setup Run Settings");
            RunType mtype1 = new RunType("Running", true);
            RunType mtype2 = new RunType("Rowing", true);
            RunType mtype3 = new RunType("Walking", true);
            mRepository.insertRunType(mtype1);
            mRepository.insertRunType(mtype2);
            mRepository.insertRunType(mtype3);
            listedRunTypes.add(mtype1);
            listedRunTypes.add(mtype2);
            listedRunTypes.add(mtype3);

            RunType mtype4 = new RunType("Cycling", false);
            RunType mtype5 = new RunType("Kayaking", false);
            RunType mtype6 = new RunType("Intervals", false);
            RunType mtype7 = new RunType("Hiking", false);
            mRepository.insertRunType(mtype4);
            mRepository.insertRunType(mtype5);
            mRepository.insertRunType(mtype6);
            mRepository.insertRunType(mtype7);
            storedRunTypes.add(mtype4);
            storedRunTypes.add(mtype5);
            storedRunTypes.add(mtype6);
            storedRunTypes.add(mtype7);

            edt.putBoolean("setup_run_type", false);
            edt.commit();
        }else{*/
            getSettingsData();
      //  }

        mLayoutManager = new LinearLayoutManager(mContext);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mAdapter = new RunTypeAdapter(listedRunTypes);
        mRecyclerView = view.findViewById(R.id.my_recycler_view_active);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(mContext, mRecyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        System.out.println("segment item clicked: "+position);
                        mAdapter.setSelected(position);
                        mAdapter.notifyDataSetChanged();
                        selected = position;
                        selectedList=0;

                        mAdapter2.clearSelected();
                        mAdapter2.notifyDataSetChanged();

                        if(listedRunTypes.get(position).getName() != null) {
                            mNameEdit.setText(listedRunTypes.get(position).getName());
                        }
                        if(listedRunTypes.get(position).getActive() != null) {
                            mSwitch.setChecked(listedRunTypes.get(position).getActive());
                        }
                        if(listedRunTypes.get(position).getDistanceUnits() != null) {
                            mSpinnerDistance.setSelection(listedRunTypes.get(position).getDistanceUnits());
                        }
                        if(listedRunTypes.get(position).getPaceUnits() != null) {
                            mSpinnerPace.setSelection(listedRunTypes.get(position).getPaceUnits());
                        }
                        if(listedRunTypes.get(position).getCalBurnValue() != null) {
                            mCalFactorEdit.setText(Double.toString(listedRunTypes.get(position).getCalBurnValue()));
                        }
                        if(listedRunTypes.get(position).getCalBurnUnit() != null) {
                            mCalUnits.setSelection(listedRunTypes.get(position).getCalBurnUnit());
                        }
                    }

                    @Override
                    public void onItemLongClick(View view, int position) {
                        System.out.println("segment item long clicked: "+position);

                    }
                })
        );

        mLayoutManager2 = new LinearLayoutManager(mContext);
        mLayoutManager2.setOrientation(LinearLayoutManager.VERTICAL);
        mAdapter2 = new RunTypeAdapter(storedRunTypes);
        mRecyclerView2 = view.findViewById(R.id.my_recycler_view_options);
        mRecyclerView2.setLayoutManager(mLayoutManager2);
        mRecyclerView2.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView2.setAdapter(mAdapter2);
        mRecyclerView2.addOnItemTouchListener(
                new RecyclerItemClickListener(mContext, mRecyclerView2, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        System.out.println("segment item clicked: "+position);
                        mAdapter2.setSelected(position);
                        mAdapter2.notifyDataSetChanged();
                        selected = position;
                        selectedList=1;

                        if(storedRunTypes.get(position).getName() != null) {
                            mNameEdit.setText(storedRunTypes.get(position).getName());
                        }
                        if(storedRunTypes.get(position).getActive() != null) {
                            mSwitch.setChecked(storedRunTypes.get(position).getActive());
                        }
                        if(storedRunTypes.get(position).getDistanceUnits() != null) {
                            mSpinnerDistance.setSelection(storedRunTypes.get(position).getDistanceUnits());
                        }
                        if(storedRunTypes.get(position).getPaceUnits() != null) {
                            mSpinnerPace.setSelection(storedRunTypes.get(position).getPaceUnits());
                        }
                        if(storedRunTypes.get(position).getCalBurnValue() != null) {
                            mCalFactorEdit.setText(Double.toString(storedRunTypes.get(position).getCalBurnValue()));
                        }
                        if(storedRunTypes.get(position).getCalBurnUnit() != null) {
                            mCalUnits.setSelection(storedRunTypes.get(position).getCalBurnUnit());
                        }

                        mAdapter.clearSelected();
                        mAdapter.notifyDataSetChanged();
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
        MaterialFancyButton deleteButton = view.findViewById(R.id.delete_button);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(getActivity());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_delete);
                DisplayMetrics metrics = getActivity().getResources().getDisplayMetrics();
                int width = metrics.widthPixels;
                dialog.getWindow().setLayout((8 * width) / 9, LinearLayout.LayoutParams.WRAP_CONTENT);
                TextView mText = dialog.findViewById(R.id.confirm_title);
                mText.setText("Permanently Delete Activity Type?");

                dialog.findViewById(R.id.delete).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        System.out.println("deleting workout");
                        //selected stores clicked position of either list
                        //selectedList = 0 for active types, 1 for stored types
                        if(selectedList==0) {
                            mRepository.deleteRunType(listedRunTypes.get(selected));
                            listedRunTypes.remove(selected);
                        }else{
                            mRepository.deleteRunType(storedRunTypes.get(selected));
                            storedRunTypes.remove(selected);
                        }
                        mAdapter.notifyDataSetChanged();
                        mAdapter2.notifyDataSetChanged();
                       /* new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                //set to location

                            }
                        }, 500);
*/


                        dialog.dismiss();
                    }
                });
                dialog.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();

            }
        });

        MaterialFancyButton saveButton = view.findViewById(R.id.save_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RunType mType = null;

                   if(selectedList==0) {
                       mType =listedRunTypes.get(selected);
                       mType.setName(mNameEdit.getText().toString());
                       if(!mSwitch.isChecked()){
                            listedRunTypes.remove(selected);
                            storedRunTypes.add(mType);
                       }
                   }else if(selectedList ==1){
                       mType =storedRunTypes.get(selected);
                       mType.setName(mNameEdit.getText().toString());
                       if(mSwitch.isChecked()){
                           listedRunTypes.add(mType);
                           storedRunTypes.remove(selected);
                       }
                   }

                    mType.setActive(mSwitch.isChecked());
                    mType.setName(mNameEdit.getText().toString());
                    mType.setDistanceUnits(mSpinnerDistance.getSelectedItemPosition());
                    mType.setPaceUnits(mSpinnerPace.getSelectedItemPosition());
                    mType.setCalBurnValue(Double.parseDouble(mCalFactorEdit.getText().toString()));
                    mType.setCalBurnUnit(mCalUnits.getSelectedItemPosition());

                    mAdapter.notifyDataSetChanged();
                    mAdapter2.notifyDataSetChanged();
                    mRepository.updateRunType(mType);
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

    @SuppressLint("StaticFieldLeak")
    private void getSettingsData() {
        new AsyncTask<Void, Void, Void>() {
            //Get today's workout => finishUI
            //get today's workout, if it doesn't exist create it

            protected void onPreExecute() {
                System.out.println("GETTING RUN TYPE SETTINGS");
            }

            protected Void doInBackground(Void... unused) {
                //nutritionDay and formatted day change with selected day up top, used to edit values
                allRunTypes = new ArrayList<RunType>(AppDatabase.getAppDatabase(getActivity()).rtDAO().getAll());
                for(int i=0; i<allRunTypes.size(); i++){
                    if(allRunTypes.get(i).getActive()){
                        listedRunTypes.add(allRunTypes.get(i));
                        System.out.println("listed run types: "+allRunTypes.get(i).getName());
                    }else{
                        storedRunTypes.add(allRunTypes.get(i));
                        System.out.println("stored run types: "+allRunTypes.get(i).getName());
                    }
                }


                return null;
            }

            protected void onPostExecute(Void unused) {
                // Post Code
                finishUI();
            }
        }.execute();
    }

    public void finishUI(){
        mAdapter.notifyDataSetChanged();
        mAdapter2.notifyDataSetChanged();


    }


}

