package android.example.com.visualizerpreferences;

import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;


public class SettingsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState); //빼 먹으면 crash..
        setContentView(R.layout.activity_visualizer);
        ActionBar actionBar = this.getSupportActionBar();


        // Set the action bar back button to look like an up button
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }


    // NavUtils.navigateUpFromSameTask(this); 라는 녀석은 좀 무지막지 하다.
    //이녀석은 현재 Activity stack의 최상위로 한방에 간다.
    //즉, A > B > C > D 식으로 이동해서 현재가 D Activity 라고 하면, 한방에 A 로 이동한다.
    //내가 지금 쓰려는건 C 로 차례대로 되돌아가는 것이므로 finish(); 로 처리했다.
    //[출처] [안드로이드] getActionBar() setDisplayHomeAsUpEnabled |작성자 여우별

//        @Override
//        public boolean onOptionsItemSelected(MenuItem item) {
//            switch (item.getItemId()) {
//                case android.R.id.home:
//                NavUtils.navigateUpFromSameTask(this);
//                finish();
//                    return true;
//            }
//            return super.onOptionsItemSelected(item);
//        }
    //NavUtils.navigateUpFromSameTask(this); 코드는 android 에서 기본적으로 추가해 주었다.
    //이놈은 현재 Activity stack 의 최상위로 이동 하면서 그 아래에 있던 Activity 들은 모조리 destroy 해버리는 무지막지한 놈이다.
    //ex) A -> B -> C -> D 의 navi 버튼 클릭 -> A

    //그래서 보통 현재 Activity 만 종료하고 상위로 이동하고 싶으면 finish(); 정도만 해주면 된다.
    //[출처] setDisplayHomeAsUpEnabled|작성자 영승

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        if(id == android.R.id.home){
            NavUtils.navigateUpFromSameTask(this);
        }
        return super.onOptionsItemSelected(item);
        //WHY return this?
    }
}
