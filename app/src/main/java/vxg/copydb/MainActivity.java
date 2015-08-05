package vxg.copydb;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;


public class MainActivity extends Activity {

    TextView tv_path ;
    EditText et_path ;
    EditText et_dbname ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btn_copy = (Button) findViewById(R.id.btn_copy);
        btn_copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                copyDb("t_vxg");
            }
        });
        tv_path = (TextView)findViewById(R.id.tv_path);
        et_path = (EditText)findViewById(R.id.path_edit);
        et_dbname = (EditText)findViewById(R.id.dbname_edit);
    }


    /*
     * copy db to sdcard
     */
    private void copyDb(String dbname){
        try {
            File sd = Environment.getExternalStorageDirectory();
            File data = Environment.getDataDirectory();

            String srcPackagename = et_path.getText().toString() ;
            String srcDBname = et_dbname.getText().toString() ;
            if ( srcPackagename == null || srcPackagename.length() <1 ){
                Toast.makeText(this, "包名不能为空", Toast.LENGTH_LONG).show();
                return;
            }
            if ( srcDBname == null || srcDBname.length() <1 ){
                Toast.makeText(this, "数据库名不能为空", Toast.LENGTH_LONG).show();
                return;
            }

            if (sd.canWrite()) {
//                String currentDBPath = "/data/data/" + getPackageName() + "/databases/vxg_data";
                String currentDBPath = "/data/data/" + getPackageName() + "/databases/"+srcDBname;
//                String currentDBPath = "/data/data/com.example." + srcPackagename + ".app/databases/"+srcDBname;
                String backupDBPath = srcDBname+".backup";
                File currentDB = new File(currentDBPath);
                File backupDB = new File(sd, backupDBPath);

                if (currentDB.exists()) {
                    FileChannel src = new FileInputStream(currentDB).getChannel();
                    FileChannel dst = new FileOutputStream(backupDB).getChannel();
                    dst.transferFrom(src, 0, src.size());
                    src.close();
                    dst.close();
                    tv_path.setText("已经备份到["+backupDB.getPath()+"]");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
