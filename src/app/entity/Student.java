package app.entity;

public class Student {
    private String id;
    private String name;
    private double mathScore;
    private double literatureScore;
    private double englishScore;

    public Student(String id, String name, double mathScore, double literatureScore, double englishScore) {
        this.id = id;
        this.name = name;
        this.mathScore = mathScore;
        this.literatureScore = literatureScore;
        this.englishScore = englishScore;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getMathScore() {
        return mathScore;
    }

    public void setMathScore(double mathScore) {
        this.mathScore = mathScore;
    }

    public double getLiteratureScore() {
        return literatureScore;
    }

    public void setLiteratureScore(double literatureScore) {
        this.literatureScore = literatureScore;
    }

    public double getEnglishScore() {
        return englishScore;
    }

    public void setEnglishScore(double englishScore) {
        this.englishScore = englishScore;
    }

    @Override
    public String toString() {
        return "Student{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", mathScore=" + mathScore +
                ", literatureScore=" + literatureScore +
                ", englishScore=" + englishScore +
                '}';
    }

    public String toCSV() {
        return id + "," + name + "," + mathScore + "," + literatureScore + "," + englishScore;
    }

    public double getAverageScore() {
        return (mathScore + literatureScore + englishScore) / 3;
    }

    public String getGrade() {
        double averageScore = getAverageScore();
        if (averageScore >= 8) {
            return "A";
        } else if (averageScore >= 7) {
            return "B";
        } else if (averageScore >= 6) {
            return "C";
        } else if (averageScore >= 5) {
            return "D";
        } else {
            return "F";
        }
    }
}
