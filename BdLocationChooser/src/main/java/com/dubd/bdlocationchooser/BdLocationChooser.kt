package com.dubd.bdlocationchooser

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.marginStart
import org.json.JSONObject

class BdLocationChooser  {


    class Create(private val context : Context){

        private var selectedDivision : Division? = null     // Variable for storing the selected division
        private var selectedDistrict : District? = null     // Variable for storing the selected district
        private var selectedUpazila : Upazila? = null     // Variable for storing the selected upazila

        // Parsing the json data of divisions, districts and upazilas
        val districtsList = JSONObject(bd_districts_string).optJSONArray("districts")
            ?.let { 0.until(it.length()).map{i -> it.optJSONObject(i)} } // returns an array of JSONObject
            ?.map { District(it.toString())} // transforms each JSONObject of the array into District Class
        val divisionsList = JSONObject(bd_divisions_string).optJSONArray("divisions")
            ?.let { 0.until(it.length()).map{i -> it.optJSONObject(i)} } // returns an array of JSONObject
            ?.map { Division(it.toString())} // transforms each JSONObject of the array into Division Class
        val upazilasList = JSONObject(bd_upazillas_string).optJSONArray("upazilas")
            ?.let { 0.until(it.length()).map{i -> it.optJSONObject(i)} } // returns an array of JSONObject
            ?.map { Upazila(it.toString())} // transforms each JSONObject of the array into Upazila Class

        var locationChooseListener : LocationChooseListener? = null     // An interface to handle the callbacks weather cancelled or a upazila was selected
        var languageEnglish = false     // This will basically display the list in english/bangla depending on your requriement (Default : Bangla)
        var title = "Choose your location"  //This is the title of the dialog, you can change it dynamically.

        // Function to change the language of the list shown (Default Bangla)
        // However the returned classes will have both bangla and english names in them
        fun setLanguage(language : String) : Create{
            languageEnglish = language.lowercase() == "english"     // Passing "english" to this will change the lang to english
            return this
        }

        // This changes the title of the dialog and sets it to whatever you like :D
        fun setPickerTitle(title : String): Create{
            this.title = title
            return this
        }

        // This set's the listener to the class
        fun setListener(listener: LocationChooseListener) : Create{
            locationChooseListener = listener
            return this
        }

        // The actual function to show the dialog
        // I know the below code is not the most efficient or readable or even good approach,
        // But I'm hoping to make it better in future updates and versions
        fun showDialog(){
            // Creates an alert dialog
            val alertDialog = AlertDialog.Builder(context).create()

            // Inflates the custom view for the dialog
            val alertDialogView = LayoutInflater.from(context).inflate(R.layout.location_chooser_layout, null)

            //Sets the view to the dialog
            alertDialog.setView(alertDialogView)

            // For removing the default bg of the dialog and making it transparent
            alertDialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)

            // Showing the alert dialog
            alertDialog.show()

            // In case the user cancels the dialog by clicking outside the window
            // Or the user clicks the backbutton or stuff
            // Then the cancellation of the dialog will call the on cancelled function of the listener
            alertDialog.setOnCancelListener {
                locationChooseListener?.onCancelled()
            }

            // The title bar text view and setting title
            val titleBarTop = alertDialogView.findViewById<TextView>(R.id.titleBarTop)
            titleBarTop.text = title

            // Three text views for showing the selected items on top
            val selectedDivisionTextView = alertDialogView.findViewById<TextView>(R.id.selectedDivisionTextView)
            val selectedDistrictTextView = alertDialogView.findViewById<TextView>(R.id.selectedDistrictTextView)
            val selectedUpazilaTextView = alertDialogView.findViewById<TextView>(R.id.selectedUpazilaTextView)

            // Initially they are not visible unless each item is selected
            selectedDivisionTextView.visibility = View.GONE
            selectedDistrictTextView.visibility = View.GONE
            selectedUpazilaTextView.visibility = View.GONE


            // For adding margin and making the radio buttons look a bit better
            val buttonLayoutParams: LinearLayout.LayoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            buttonLayoutParams.setMargins(10, 6, 10, 6)
            buttonLayoutParams.width = LinearLayout.LayoutParams.MATCH_PARENT

            // This is the linear layout that'll contain the main radio group
            val mainLinearItemsListContainer = alertDialogView.findViewById<LinearLayout>(R.id.mainLinearItemsListContainer)

            // This is the main radio group
            val radioGroup = RadioGroup(context)

            radioGroup.orientation = RadioGroup.VERTICAL
            mainLinearItemsListContainer.addView(radioGroup)    // Adding the radio group to the view

            // For each of the divisions we'll create a radio button and add it to the radio group
            for(item in divisionsList!!){
                val radioButton = RadioButton(context)

                // Here each of the division's model class already has an integer id, so we can
                // Simply use that Id to identify the selected radio button later on
                radioButton.id = item.id

                // Setting the radiobutton text depending on the language
                radioButton.text = if(languageEnglish){ item.name }else{ item.bengaliName }

                // Padding to look better :D
                radioButton.setPadding(10, 10, 10, 10)

                // Setting the predefined layout params
                radioButton.layoutParams = buttonLayoutParams

                // Text styles
                radioButton.setTextColor(Color.parseColor("#ffffff"))

                // Adding a custom selector to the radio button which is not entirely used in this
                // version of the library
                radioButton.background = ResourcesCompat.getDrawable(context.resources, R.drawable.radio_selector, context.resources.newTheme())

                // Adding the radio button to the radio group and the loop goes on for others
                radioGroup.addView(radioButton)
            }

            // Adding a checked change listener for knowing when a radio button is selected
            radioGroup.setOnCheckedChangeListener { _, rbIdDivision ->
                // as the radio buttons id and the divisions id is same, so we just filter the divisions
                // List by the ids and find the selected one
                selectedDivision = divisionsList.filter { it.id == rbIdDivision }[0]

                // After finding the selected division we make the division textview visible
                selectedDivisionTextView.visibility = View.VISIBLE
                // Then set the selected division name to the text view depending on the selected language
                selectedDivisionTextView.text = if(languageEnglish){
                    selectedDivision!!.name
                }else{
                    selectedDivision!!.bengaliName
                }

                // Then we remove the division radio buttons that we created and make the radio group empty
                radioGroup.removeAllViews()

                // Then we use the same radio group for the districts and upazilas as we did for
                // the divisions and the process goes on till the upazila is selected
                for(item in districtsList!!.filter { it.divisionId == rbIdDivision }){
                    val radioButton = RadioButton(context)
                    radioButton.id = item.id
                    radioButton.text = if(languageEnglish){ item.name }else{ item.bengaliName }
                    radioButton.layoutParams = buttonLayoutParams
                    radioButton.setPadding(10, 10, 10, 10)
                    radioButton.setTextColor(Color.parseColor("#ffffff"))
                    radioButton.background = ResourcesCompat.getDrawable(context.resources, R.drawable.radio_selector, context.resources.newTheme())
                    radioGroup.addView(radioButton)
                }
                radioGroup.setOnCheckedChangeListener { _, rbIdDistricts ->
                    selectedDistrict = districtsList.filter { it.id == rbIdDistricts }[0]
                    selectedDistrictTextView.visibility = View.VISIBLE
                    selectedDistrictTextView.text = if (languageEnglish) {
                        selectedDistrict!!.name
                    } else {
                        selectedDistrict!!.bengaliName
                    }
                    radioGroup.removeAllViews()
                    for(item in upazilasList!!.filter { it.districtId == rbIdDistricts }){
                        val radioButton = RadioButton(context)
                        radioButton.id = item.id
                        radioButton.text = if(languageEnglish){ item.name }else{ item.bengaliName }
                        radioButton.setPadding(10, 10, 10, 10)
                        radioButton.setTextColor(Color.parseColor("#ffffff"))
                        radioButton.layoutParams = buttonLayoutParams
                        radioButton.background = ResourcesCompat.getDrawable(context.resources, R.drawable.radio_selector, context.resources.newTheme())
                        radioGroup.addView(radioButton)
                    }
                    radioGroup.setOnCheckedChangeListener { _, rbIdUpazilas ->
                        selectedUpazila = upazilasList.filter { it.id == rbIdUpazilas }[0]
                        selectedUpazilaTextView.visibility = View.VISIBLE
                        selectedUpazilaTextView.text = if (languageEnglish) {
                            selectedUpazila!!.name
                        } else {
                            selectedUpazila!!.bengaliName
                        }

                        // This is where the upazila is selected and we're confirmed that we have all the three required values,
                        // So we dismiss the dialog
                        alertDialog.dismiss()
                        // And then we call the function of location choosen and return the required values to the callback
                        locationChooseListener?.onLocationChoose(selectedDivision!!, selectedDistrict!!, selectedUpazila!!)
                    }
                }
            }
        }
    }

}