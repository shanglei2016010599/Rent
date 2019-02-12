package com.zwu.rent;

public class House {

    private String ID ;
    private String State;
    private String Name;
    private String Price;
    private String Address;
    private String Detail ;
    private String Uploadtime;
    private String ImageUrl;

    private String title;
    private String detail;
    private String Url;

    final private String url = "http://106.15.190.83:8000";
    final private String image = "http://106.15.190.83:8000/images/house/1.jpg";

    public House(String ID, String State, String Name, String Price, String Address,
                 String Detail, String Uploadtime, String ImageUrl){
        this.ID = ID;
        this.State = State;
        this.Name = Name;
        this.Price = Price;
        this.Address = Address;
        this.Detail = Detail;
        this.Uploadtime = Uploadtime;
        this.ImageUrl = ImageUrl;

        if ( State.equals("1") ) {
            this.title = "可预约 " + Price + "元";
        }
        else if ( State.equals("3") ){
            this.title = "已预约 " + Price + "元";
        }
        this.detail = "名称:" + Name + "\r\n"
                + "价格:" + Price + "\r\n"
                + "详情:" + Detail + "\r\n"
                + "地址:" + Address + "\r\n"
                + "更新时间:" + Uploadtime;

        if ( !ImageUrl.equals("") ){
            this.Url = url + ImageUrl.substring(1);
        }
        else{
            this.Url = image;
        }
    }

    public String getID(){
        return ID;
    }

    public String getState(){
        return State;
    }

    public String getName(){
        return Name;
    }

    public String getPrice(){
        return Price;
    }

    public String getAddress(){
        return Address;
    }

    public String getDetail(){
        return Detail;
    }

    public String getUploadtime(){
        return Uploadtime;
    }

    public String getImageUrl(){
        return ImageUrl;
    }

    public String getTitle(){
        return title;
    }

    public String getUrl(){
        return image;
    }

    public String getdetail(){
        return detail;
    }

    public String getImage(){return image;}

    public void setID(String ID){this.ID = ID;}

    public void setState(String State){this.State = State;}

    public void setName(String Name){this.Name = Name;}

    public void setPrice(String Price){this.Price = Price;}

    public void setAddress(String Address){this.Address = Address;}

    public void setDetail(String Detail){this.Detail = Detail;}

    public void setUploadtime(String Uploadtime){this.Uploadtime = Uploadtime;}

    public void setImageUrl(String ImageUrl){this.ImageUrl = ImageUrl;}

    public void setTitle(){
        if ( State.equals("1") ) {
            this.title = "可预约 " + Price + "元";
        }
        else if ( State.equals("3") ){
            this.title = "已预约 " + Price + "元";
        }
    }

    public void setUrl(){
        if ( !ImageUrl.equals("") ){
            this.Url = url + ImageUrl.substring(1);
        }
        else{
            this.Url = image;
        }
    }

    public void setdetail(){
        this.detail = "名称:" + Name + "\r\n"
                + "价格:" + Price + "\r\n"
                + "详情:" + Detail + "\r\n"
                + "地址:" + Address + "\r\n"
                + "更新时间:" + Uploadtime;
    }

    public void Set(String ID, String State, String Name, String Price, String Address,
                     String Detail, String Uploadtime, String ImageUrl){
        this.ID = ID;
        this.State = State;
        this.Name = Name;
        this.Price = Price;
        this.Address = Address;
        this.Detail = Detail;
        this.Uploadtime = Uploadtime;
        this.ImageUrl = ImageUrl;

        if ( State.equals("1") ) {
            this.title = "可预约 " + Price + "元";
        }
        else if ( State.equals("3") ){
            this.title = "已预约 " + Price + "元";
        }
        this.detail = "名称:" + Name + "\r\n"
                + "价格:" + Price + "\r\n"
                + "详情:" + Detail + "\r\n"
                + "地址:" + Address + "\r\n"
                + "更新时间:" + Uploadtime;

        if ( !ImageUrl.equals("") ){
            this.Url = url + ImageUrl.substring(1);
        }
        else{
            this.Url = image;
        }
    }

}
