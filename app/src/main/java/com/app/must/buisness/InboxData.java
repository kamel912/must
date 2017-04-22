package com.app.must.buisness;

public class InboxData {
    private String body;
    private int dept_id;
    private String emp_code;
    private int msg_id;
    private boolean read;
    private String send_date;
    private String subject;

    public int getMsgID() {
        return this.msg_id;
    }

    public void setMsgID(int id) {
        this.msg_id = id;
    }

    public int getDeptID() {
        return this.dept_id;
    }

    public void setDeptID(int id) {
        this.dept_id = id;
    }

    public String getSubject() {
        return this.subject;
    }

    public void setSubject(String title) {
        this.subject = title;
    }

    public String getBody() {
        return this.body;
    }

    public void setBody(String title) {
        this.body = title;
    }

    public String getEmpcode() {
        return this.emp_code;
    }

    public void setEmpcode(String title) {
        this.emp_code = title;
    }

    public String getSendDate() {
        return this.send_date;
    }

    public void setSendDate(String title) {
        this.send_date = title;
    }

    public boolean getRead() {
        return this.read;
    }

    public void setRead(boolean title) {
        this.read = title;
    }
}
