package domain;

import util.BinarySemaphore;

import java.util.ArrayList;
import java.util.HashMap;

class Section {
    int quota;
    ArrayList<Integer> hours;

    Section(int quota) {
        this.quota = quota;
        this.hours = new ArrayList<Integer>();
    }

    public int getQuota() {
        return quota;
    }

    public void setQuota(int quota) {
        this.quota = quota;
    }

    public ArrayList<Integer> getHours() {
        ArrayList<Integer> hours = new ArrayList<Integer>();
        hours.addAll(hours);
        return hours;
    }

    public void setHours(ArrayList<Integer> hours) {
        this.hours.clear();
        this.hours.addAll(hours);
    }

    public boolean add() {
        if (quota > 0) {
            quota--;
            return true;
        }

        return false;
    }

    public void drop() {
        quota++;
    }
}

public class Course {
    private String code;
    private ArrayList<Section> sections;
    public BinarySemaphore lock;

    public Course(String code) {
        this.code = code;
        sections = new ArrayList<Section>();
        this.lock = new BinarySemaphore(true);
    }

    public Course(String code, int numOfSections, int quota) {
        this.code = code;
        sections = new ArrayList<Section>();
        this.lock = new BinarySemaphore(true);

        for (int i=0; i<numOfSections; i++) {
            addSection(quota);
        }
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public boolean hasSection(int section) {
        return section >= 0 && section < sections.size();
    }

    public HashMap<Integer, Integer> getSections() {
        HashMap<Integer, Integer> res = new HashMap<Integer, Integer>();

        for (int i=0; i<sections.size(); i++) {
            res.put(i, sections.get(i).getQuota());
        }

        return res;
    }

    public Integer getQuota(int section) {
        return sections.get(section).getQuota();
    }

    public void addSection(int quota) {
        this.sections.add(new Section(quota));
    }

    public void addCourse(int section) {
        sections.get(section).add();
    }

    public void  dropCourse(int section) {
        sections.get(section).drop();
    }

    public void changeSection(int oldSection, int newSection) {
        System.out.println(oldSection + " dropped ==== added" + newSection);
        sections.get(oldSection).drop();
        sections.get(newSection).add();
    }
}
