package mx.diego.charts;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import mx.diego.donutchart.Donut;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Donut donut = (Donut) findViewById(R.id.donut);

        List<Float> percentages = new ArrayList<>();
        percentages.add(0.3f);
        percentages.add(0.2f);
        assert donut != null;
        donut.setPercentages(percentages);
    }
}
