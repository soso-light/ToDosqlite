import java.sql.*;

public class Main {
    //create table : table 만드는 sql 테이블이 없다면 만들어라.
    final String CREATETBL = "create table if not exists todo (" +
            "id integer primary key autoincrement, " +
            "title text not null, " +
            "context text not null, " +
            "deadline text not null, " +
            "regdate datetime DEFAULT (datetime('now','localtime'))" +
            ")";

    public static void main(String args[]){
        Main main = new Main();
        main.sqliteFunc();
    }
    //
    void sqliteFunc(){
        //최소한의 클래스
        //연결
        Connection conn = null;
        //실행(어떤 것이든 다 필요해)
        Statement stmt = null;
        //조작
        PreparedStatement pstmt = null;

        // sqlite jdbc(자바 디비 커넥트)가 있는지 클래스 확인
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try {
            //test.db 데이터베이스 연결(없으면 생성)
            //저 유알엘이 포맷. 디비가 없으련 저 이름으로 만들어라. 테스트만 바꾸면 디비명 벼ㄹ경
            conn = DriverManager.getConnection("jdbc:sqlite:test.db");

            //테이블 생성 : SQL문 실행 이 쿼리 시행해 (테이ㅡ블 만들게
            // )
            stmt = conn.createStatement();
            stmt.execute(CREATETBL);

            //data 삭제
            String sql0 = "delete from todo";
            stmt.execute(sql0);

            //데이터 추가 : Statment 사용
            String sql = "insert into todo (title, context, deadline) values ('자바', '과제 제출', '6/30')";
            stmt.execute(sql);

            //데이터 추가 : PreparedStatement 사용
            String sql2 = "insert into todo (title, context, deadline) values(?,?,?)";
            pstmt = conn.prepareStatement(sql2);
            pstmt.setString(1, "정리");
            pstmt.setString(2, "비행기");
            pstmt.setString(3, "7/10");
            pstmt.executeUpdate();

            //데이터 조회 : SQL문 실행
            ResultSet rs = stmt.executeQuery("select * from todo");
            while(rs.next()) {
                int id = rs.getInt("id");
                String title = rs.getString("title");
                String context = rs.getString("context");
                String regdate = rs.getString("regdate");
                String deadline = rs.getString("deadline");
                System.out.println(id + " " + regdate + " " + deadline + " " + title + " " + context);
            }
            rs.close();
            pstmt.close();
            stmt.close();
            conn.close();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
