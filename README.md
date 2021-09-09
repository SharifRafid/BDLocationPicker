# BDLocationChooser
 An Android library for picking location in Bangladesh
 
 
 Example Screenshot : 



**Implement Library** 

The library is available on JitPack, for implementing, just go to your project-level
or root level build.gradle file and add the below code if not added already :

```
allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' } // Add This Line
		}
	}
```
 
 Then go to your app level build.gradle file and add this line : 
```
dependencies {
	  implementation 'com.github.SharifRafid:BDLocationPicker:1.0.0' //Add this line in the dependencies
	}
```

**Usage Of Library**

Java : 
```
 new BdLocationChooser.Create(this)
                .setPickerTitle("Choose a location")
                .setLanguage("english")     // Default Bangla
                .setListener(new LocationChooseListener() {
                    @Override
                    public void onLocationChoose(@NonNull Division division, @NonNull District district, @NonNull Upazila upazila) {
                        Toast.makeText(JavaActivity.this, division.getName()+","+district.getName()+","+upazila.getName(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled() {
                        Log.e("LocationPicker","Cancelled");
                    }
                }).showDialog();
```
Kotlin : 
```
BdLocationChooser.Create(this)
        .setPickerTitle("Choose a location")
        .setLanguage("english")     // Default Bangla
        .setListener(object : LocationChooseListener{
            override fun onLocationChoose(division: Division,district: District,upazila: Upazila) {
                    Toast.makeText(this@MainActivity, division.name+","+district.name+","+upazila.name,Toast.LENGTH_SHORT).show()
            }

            override fun onCancelled() {
              Log.e("LocationPicker","Cancelled")
            }

         }).showDialog()
```

Or you can also use the library to get the lists of the divisions/districts/upazilas 
and make your own custom picker with the data :

Example : 

Java : 
```

// Get All the upazilas
ArrayList<Upazila> upazilasList = new BdLocationChooser.Create(this).getUpazilasList();

// Get All the districts
ArrayList<District> districtsList = new BdLocationChooser.Create(this).getDistrictsList();

// Get All the divisions
ArrayList<Division> divisionsList = new BdLocationChooser.Create(this).getDivisionsList();
```

Kotlin:
```
// Get All the upazilas
val upazilaList = BdLocationChooser.Create(this).upazilasList as ArrayList<Upazila>

// Get All the districts
val districtList = BdLocationChooser.Create(this).districtsList as ArrayList<District>

// Get All the divisions
val divisionList = BdLocationChooser.Create(this).divisionsList as ArrayList<Division>
```

The json formatted data of the divisions/districts/upazillas are collected from [fahimxyz](https://github.com/fahimxyz) and his awesome repo : https://github.com/fahimxyz/bangladesh-geojson .

Thanks A Lot to [fahimxyz](https://github.com/fahimxyz) for his hardwork.

