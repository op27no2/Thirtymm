package op27no2.fitness.Centurion2.fragments.run;

public class AlertItem {

    public AlertItem(Integer conditionType, Integer alertType){
        this.conditionType = conditionType;
        this.alertType = alertType;
    }

    private Integer conditionType;
    private Integer alertType;

    public void setConditionType(Integer conditionType){
        this.conditionType = conditionType;
    }
    public Integer getConditionType(){
        return conditionType;
    }

    public void setAlertType(Integer alertType){
        this.alertType = alertType;
    }
    public Integer getAlertType(){
        return alertType;
    }


}
