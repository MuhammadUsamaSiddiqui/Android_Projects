/**
 * IMPORTANT: Make sure you are using the correct package name.
 * This example uses the package name:
 * package com.example.android.justjava
 * If you get an error when copying this code into Android studio, update it to match teh package name found
 * in the project's AndroidManifest.xml file.
 **/

package com.example.muhammadusama.justjava;



import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;

import static com.example.muhammadusama.justjava.R.id.checkbox;
import static com.example.muhammadusama.justjava.R.id.quantity_text;

/**
 * This app displays an order form to order coffee.
 */
public class MainActivity extends AppCompatActivity {

    int quantity = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * This method is called when the order button is clicked.
     */
    public void submitOrder(View view) {

        EditText Name = (EditText) findViewById(R.id.name);
        String customerName=Name.getText().toString();
        if (customerName.equals("")) {

            Toast.makeText(this, "Enter Your Name", Toast.LENGTH_SHORT).show();
        } else {

            CheckBox whippedCream = (CheckBox) findViewById(R.id.whipped_cream);
            CheckBox chocolateTopping = (CheckBox) findViewById(R.id.chocolate);
            display(quantity);
            int price = calculatePrice(whippedCream.isChecked(), chocolateTopping.isChecked());
            String summary = createOrderSummary(price, whippedCream.isChecked(), chocolateTopping.isChecked(),customerName);
           // displayMessage(summary);

            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("mailto:")); // only email apps should handle this
            intent.putExtra(Intent.EXTRA_SUBJECT, "Just Java Order For " + customerName);
            intent.putExtra(Intent.EXTRA_TEXT,summary);
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            }

        }


/*   Intent for Map
          Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("geo:47.6,-122.3"));
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            }*/

    }

    private int calculatePrice(boolean addWhippedCream,boolean addChocolate){

        int basePrice =5;
        if(addWhippedCream){

            basePrice = basePrice +1;
        }

        if (addChocolate){

            basePrice = basePrice +2;
        }

        return quantity * basePrice;
    }

    /*
    Method For Increment Button
    */

    public void increment(View view){

       if (quantity==100){
           Toast.makeText(this, "You can not order coffee more than 100 Cups", Toast.LENGTH_SHORT).show();
       }else {
           quantity++;
           display(quantity);
       }
    }

      /*
    Method For Decrement Button
    */

    public void decrement(View view){

        if (quantity==1){
            Toast.makeText(this, "You can not order coffee less than 1 Cup", Toast.LENGTH_SHORT).show();
        }else {
            quantity--;
            display(quantity);

        }
    }

    /**
     * This method displays the given quantity value on the screen.
     */
    private void display(int number) {
        TextView quantityTextView = (TextView) findViewById(R.id.quantity_text);
        quantityTextView.setText("" + number);
    }

    /**
     * This method displays the given price on the screen.
     */
//    private void displayMessage(String summary) {
//        TextView ordersummaryTextView = (TextView) findViewById(R.id.summary);
//        //  priceTextView.setText("Total: " + NumberFormat.getCurrencyInstance().format(number) + "\nThank You!");
//        ordersummaryTextView.setText(summary);
//    }


    private String createOrderSummary(int price,boolean addWhippedCream,boolean addChocolate,String name){

        String summary = "";
        summary += "Name : "+name+"\nAdd Whipped Cream? "+addWhippedCream+"\nAdd Chocolate? "+addChocolate+"\nQuantity : "+quantity+"\nTotal : $"+price+"\nThank You!";
        return summary;
    }
}