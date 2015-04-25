package resources;

public class CruiseControl {

	private int currentSpeed;
	private int currentCeiling;
	private int lastCeiling;
	
	
	public CruiseControl() {
		this.currentSpeed = -1;
		this.currentCeiling = -1;
		this.lastCeiling = -1;
	}
	
	public CruiseControl(CruiseControl cc){
		this.currentCeiling = cc.currentCeiling;
		this.currentSpeed = cc.currentSpeed;
		this.lastCeiling = cc.lastCeiling;
	}
	public int getCurrentSpeed() {
		return currentSpeed;
	}
	public void setCurrentSpeed(int currentSpeed) {
		this.currentSpeed = currentSpeed;
	}
	public int getCurrentCeiling() {
		return currentCeiling;
	}
	public void setCurrentCeiling(int currentCeiling) {
		this.currentCeiling = currentCeiling;
	}
	public int getLastCeiling() {
		return lastCeiling;
	}
	public void setLastCeiling(int lastCeiling) {
		this.lastCeiling = lastCeiling;
	}
	
	public void turnOff(){
		this.currentSpeed = -1;
		this.currentCeiling = -1;
		this.lastCeiling = -1;
	}
	
	
	public void control(){
		this.currentCeiling = currentSpeed;
		this.lastCeiling = currentCeiling;
	}
	
	public void pause(){
		this.currentCeiling = -1;
	}
	
	public void resume(){
		this.currentCeiling = this.lastCeiling;
	}
	
	public void increaseCeiling(){
		this.currentCeiling++;
		this.lastCeiling = currentCeiling;
	}
	
	public void decreaseCeiling(){
		this.currentCeiling--;
		this.lastCeiling = currentCeiling;
	}
	
	public String checkSpeed(){
		if(currentSpeed > currentCeiling && currentCeiling != -1){
			return "Slow down Bro";
		}
		else{
			return "You go fine";
		}
	}
}
