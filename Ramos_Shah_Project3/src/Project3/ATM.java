package Project3;
import javax.persistence.Entity;

@Entity 

public class ATM {
	private int AtmId;
	private String AtmLocation;
	private float AtmFee;
	private static boolean AtmPinVerify;

	public int getAtmId() {
		return AtmId;
	}

	public void setAtmId(int atmId) {
		AtmId = atmId;
	}

	public String getAtmLocation() {
		return AtmLocation;
	}

	public void setAtmLocation(String atmLocation) {
		AtmLocation = atmLocation;
	}

	public float getAtmFee() {
		return AtmFee;
	}

	public void setAtmFee(float atmFee) {
		AtmFee = atmFee;
	}

	public static boolean isAtmPinVerify() {
		return AtmPinVerify;
	}


	public static void setAtmPinVerify(boolean atmPinVerify) {
		AtmPinVerify = atmPinVerify;
	}
		

}
