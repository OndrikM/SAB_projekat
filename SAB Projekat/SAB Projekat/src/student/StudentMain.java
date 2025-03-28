package student;


import rs.etf.sab.operations.*;
import rs.etf.sab.tests.TestHandler;
import rs.etf.sab.tests.TestRunner;


public class StudentMain {

    public static void main(String[] args) {
        CityOperations cityOperations = new om200335_City(); 
        DistrictOperations districtOperations = new om200335_District(); 
        CourierOperations courierOperations = new om200335_Courier(); 
        CourierRequestOperation courierRequestOperation = new om200335_CourierRequest();
        GeneralOperations generalOperations = new om200335_General();
        UserOperations userOperations = new om200335_User();
        VehicleOperations vehicleOperations = new om200335_Vehicle();
        PackageOperations packageOperations = new om200335_Package();
   
       
        TestHandler.createInstance(
                cityOperations,
                courierOperations,
                courierRequestOperation,
                districtOperations,
                generalOperations,
                userOperations,
                vehicleOperations,
                packageOperations);

        TestRunner.runTests(); 
        
        
      
    }
}
