import java.sql.*;
import java.util.Properties;
import com.amazonaws.athena.jdbc.AthenaDriver;
import com.amazonaws.auth.PropertiesFileCredentialsProvider;

/* Source/Credits : AWS Athena JDBC Demo */

public class AthenaJDBCTest01 {

  static final String athenaUrl = "jdbc:awsathena://athena.us-east-1.amazonaws.com:443";

  public static void main(String[] args) {

      Connection conn = null;
      Statement statement = null;

      try {
          Class.forName("com.amazonaws.athena.jdbc.AthenaDriver");
          Properties info = new Properties();

          // Replace with a s3 bucket in your account
          info.put("s3_staging_dir", "s3://aws-athena-query-results-759133634148-us-east-1/output/");

          // ocal log file
          info.put("log_path", "./athena_jdbc.log");

          info.put("aws_credentials_provider_class","com.amazonaws.auth.PropertiesFileCredentialsProvider");

          // Local file containing AWS credentials
          info.put("aws_credentials_provider_arguments","athena_creds");

          // Database name
          String databaseName = "sampledata";

          System.out.println("Connecting to Athena...");
          conn = DriverManager.getConnection(athenaUrl, info);

          System.out.println("Listing tables...");
          String sql = "show tables in "+ databaseName;
          statement = conn.createStatement();
          ResultSet rs = statement.executeQuery(sql);

          while (rs.next()) {
              //Retrieve table column.
              String name = rs.getString("tab_name");

              //Display values.
              System.out.println("Name: " + name);
          }
          rs.close();
          conn.close();
      } catch (Exception ex) {
          ex.printStackTrace();
      } finally {
          try {
              if (statement != null)
                  statement.close();
          } catch (Exception ex) {

          }
          try {
              if (conn != null)
                  conn.close();
          } catch (Exception ex) {

              ex.printStackTrace();
          }
      }
  }
}