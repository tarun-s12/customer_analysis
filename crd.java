import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.JFrame;
import javax.swing.JPanel;

class ChartPanel extends JPanel {
  private double[] values;

  private String[] names;

  private String title;

  public ChartPanel(double[] v, String[] n, String t) {
    names = n;
    values = v;
    title = t;
  }

  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    if (values == null || values.length == 0)
      return;
    double minValue = 0;
    double maxValue = 0;
    for (int i = 0; i < values.length; i++) {
      if (minValue > values[i])
        minValue = values[i];
      if (maxValue < values[i])
        maxValue = values[i];
    }

    Dimension d = getSize();
    int clientWidth = d.width;
    int clientHeight = d.height;
    int barWidth = clientWidth / values.length;

    Font titleFont = new Font("SansSerif", Font.BOLD, 20);
    FontMetrics titleFontMetrics = g.getFontMetrics(titleFont);
    Font labelFont = new Font("SansSerif", Font.PLAIN, 10);
    FontMetrics labelFontMetrics = g.getFontMetrics(labelFont);

    int titleWidth = titleFontMetrics.stringWidth(title);
    int y = titleFontMetrics.getAscent();
    int x = (clientWidth - titleWidth) / 2;
    g.setFont(titleFont);
    g.drawString(title, x, y);

    int top = titleFontMetrics.getHeight();
    int bottom = labelFontMetrics.getHeight();
    if (maxValue == minValue)
      return;
    double scale = (clientHeight - top - bottom) / (maxValue - minValue);
    y = clientHeight - labelFontMetrics.getDescent();
    g.setFont(labelFont);

    for (int i = 0; i < values.length; i++) {
      int valueX = i * barWidth + 1;
      int valueY = top;
      int height = (int) (values[i] * scale);
      if (values[i] >= 0)
        valueY += (int) ((maxValue - values[i]) * scale);
      else {
        valueY += (int) (maxValue * scale);
        height = -height;
      }

      g.setColor(Color.red);
      g.fillRect(valueX, valueY, barWidth - 2, height);
      g.setColor(Color.black);
      g.drawRect(valueX, valueY, barWidth - 2, height);
      int labelWidth = labelFontMetrics.stringWidth(names[i]);
      x = i * barWidth + (barWidth - labelWidth) / 2;
      g.drawString(names[i], x, y);
    }
  }
}

class Visual extends Frame implements ActionListener {
    Label logo,slogan;
    Button age_stat,feed_stat,plan_stat,impr_stat,back;

    Visual() {
        logo = new Label("Spotify Inc.");
        slogan = new Label("Customer Relationship Diagnosis");

        age_stat = new Button("Age-wise stat");
        feed_stat = new Button("Feedback-wise stat");
        plan_stat = new Button("Plan-wise stat");
        impr_stat = new Button("Improvement stat");
        back = new Button("Back");

        logo.setBounds(290, 80, 200, 40);
        slogan.setBounds(235, 140, 350, 20);
        age_stat.setBounds(50,300,200,50);
        feed_stat.setBounds(300,300,200,50);
        plan_stat.setBounds(550,300,200,50);
        impr_stat.setBounds(300,400,200,50);
        back.setBounds(340,500,70,50);

        logo.setBackground(new Color(152,251,152));
        slogan.setBackground(new Color(152,251,152));
        age_stat.setBackground(new Color(173, 216, 230));
        plan_stat.setBackground(new Color(173, 216, 230));
        feed_stat.setBackground(new Color(173, 216, 230));
        impr_stat.setBackground(new Color(173, 216, 230));
        back.setBackground(Color.LIGHT_GRAY);

        Font font = new Font("Arial", Font.BOLD, 30);
        Font sfont = new Font("Times New Roman",Font.ITALIC,20);
        Font bfont = new Font("Verdana",Font.BOLD,12);
        logo.setFont(font);
        slogan.setFont(sfont);
        age_stat.setFont(bfont);
        age_stat.setForeground(Color.WHITE);
        age_stat.setFont(bfont);
        feed_stat.setForeground(Color.WHITE);
        feed_stat.setFont(bfont);
        plan_stat.setForeground(Color.WHITE);
        plan_stat.setFont(bfont);
        impr_stat.setForeground(Color.WHITE);
        impr_stat.setFont(bfont);
        back.setFont(bfont);

        age_stat.addActionListener(this);
        feed_stat.addActionListener(this);
        plan_stat.addActionListener(this);
        impr_stat.addActionListener(this);
        back.addActionListener(this);

        add(logo);
        add(slogan);
        add(age_stat);
        add(feed_stat);
        add(plan_stat);
        add(impr_stat);
        add(back);

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        setSize(800, 600);
        setTitle("Visualize");
        setLayout(null);
        setVisible(true);
        setLocationRelativeTo(null);
        setBackground(new Color(152,251,152));

    }
    public void actionPerformed(ActionEvent ae) {
        if(ae.getActionCommand().equals("Back")) {
            setVisible(false);
            dispose();
        } else if(ae.getActionCommand().equals("Age-wise stat")) {
            age_stat();
        } else if(ae.getActionCommand().equals("Feedback-wise stat")) {
            feed_stat();
        } else if(ae.getActionCommand().equals("Plan-wise stat")) {
            plan_stat();
        } else if(ae.getActionCommand().equals("Improvement stat")) {
            impr_stat();
        }
    }

    void age_stat() {
        double[] values = new double[3];
        String[] names = new String[3];
        int i = 0,youngCount = 0,adultCount = 0,elderCount = 0;
        try {
                Class.forName("oracle.jdbc.driver.OracleDriver");
                Connection con = DriverManager.getConnection("jdbc:oracle:thin:@url", "user", "password");
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery("select age,hours from statistics");
                while(rs.next()){
                  int age = rs.getInt("age");
                  if(age > 15 && age < 30) {
                    youngCount++;
                    values[0] = youngCount;
                  } else if(age > 29 && age < 46) {
                    adultCount++;
                    values[1] = adultCount;
                  } else {
                    elderCount++;
                    values[2] = elderCount;
                  }
                }
        } catch(Exception e){
                System.out.println(e);
        }
        JFrame f = new JFrame();
        f.setSize(400, 300);

        names[0] = "16-29";
        names[1] = "30-45";
        names[2] = "45 and above";

        f.getContentPane().add(new ChartPanel(values, names, "Age-wise Statistics"));

        WindowListener wndCloser = new WindowAdapter() {
          public void windowClosing(WindowEvent e) {
            System.exit(0);
          }
        };
        f.addWindowListener(wndCloser);
        f.setVisible(true);
    }

    void plan_stat() {
        double[] values = new double[3];
        String[] names = new String[3];
        int i = 0;
        try {
                Class.forName("oracle.jdbc.driver.OracleDriver");
                Connection con = DriverManager.getConnection("jdbc:oracle:thin:@url", "user", "password");
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery("select count(plan_id) as count from billing group by plan_id order by plan_id");
                while(rs.next()){
                  values[i] = rs.getInt("count");
                  i=i+1;
                }
        } catch(Exception e){
                System.out.println(e);
        }
        JFrame f = new JFrame();
        f.setSize(400, 300);

        names[0] = "Individual";
        names[1] = "Student";
        names[2] = "Family";

        f.getContentPane().add(new ChartPanel(values, names, "Age-wise Statistics"));

        WindowListener wndCloser = new WindowAdapter() {
          public void windowClosing(WindowEvent e) {
            System.exit(0);
          }
        };
        f.addWindowListener(wndCloser);
        f.setVisible(true);
    }

    void feed_stat() {
        double[] values = new double[3];
        String[] names = new String[3];
        int positiveCount=0,negativeCount=0,constructiveCount=0;
        int i = 0;
        try {
                Class.forName("oracle.jdbc.driver.OracleDriver");
                Connection con = DriverManager.getConnection("jdbc:oracle:thin:@url", "user", "password");
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery("select feedback_text from feedback");
                while(rs.next()){
                    String value = rs.getString("feedback_text");
                    if(value.equals("great") || value.equals("good") || value.equals("nice") || value.equals("well made")) {
                        positiveCount++;
                        values[0] = positiveCount;
                    } else if(value.equals("bad") || value.equals("worst") || value.equals("poor")) {
                        negativeCount++;
                        values[1] = negativeCount;
                    } else if(value.equals("improve") || value.equals("buggy") || value.equals("crash")) {
                        constructiveCount++;
                        values[2] = constructiveCount;
                    }
                  
                }
        } catch(Exception e){
                System.out.println(e);
        }
        JFrame f = new JFrame();
        f.setSize(400, 300);

        names[0] = "Positive";
        names[1] = "Negative";
        names[2] = "Constructive";

        f.getContentPane().add(new ChartPanel(values, names, "Feedback-wise Statistics"));

        WindowListener wndCloser = new WindowAdapter() {
          public void windowClosing(WindowEvent e) {
            System.exit(0);
          }
        };
        f.addWindowListener(wndCloser);
        f.setVisible(true);
    }

    void impr_stat() {
        double[] values = new double[3];
        String[] names = new String[3];
        int positiveCount=0,negativeCount=0,constructiveCount=0;
        int i = 0;
        try {
                Class.forName("oracle.jdbc.driver.OracleDriver");
                Connection con = DriverManager.getConnection("jdbc:oracle:thin:@url", "user", "password");
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery("select new_feedback from backup");
                while(rs.next()){
                    String value = rs.getString("new_feedback");
                    if(value.equals("great") || value.equals("good") || value.equals("nice") || value.equals("well made")) {
                        positiveCount++;
                        values[0] = positiveCount;
                    } else if(value.equals("bad") || value.equals("worst") || value.equals("poor")) {
                        negativeCount++;
                        values[1] = negativeCount;
                    } else if(value.equals("improve") || value.equals("buggy") || value.equals("crashy") || value.equals("fix errors")) {
                        constructiveCount++;
                        values[2] = constructiveCount;
                    }
                  
                }
        } catch(Exception e){
                System.out.println(e);
        }
        JFrame f = new JFrame();
        f.setSize(400, 300);

        names[0] = "Positive";
        names[1] = "Negative";
        names[2] = "Constructive";

        f.getContentPane().add(new ChartPanel(values, names, "Improvement in Performance"));

        WindowListener wndCloser = new WindowAdapter() {
          public void windowClosing(WindowEvent e) {
            System.exit(0);
          }
        };
        f.addWindowListener(wndCloser);
        f.setVisible(true);
    }
}

class Connect extends Frame implements ActionListener {

    Label logo, slogan;
    Button add_btn, update, back;
    int new_feed_id = 0;

    Connect() {

        logo = new Label("Spotify Inc.");
        slogan = new Label("Customer Relationship Diagnosis");

        add_btn = new Button("Add");
        update = new Button("Update");
        back = new Button("Back");

        logo.setBounds(270, 100, 200, 50);
        slogan.setBounds(200, 140, 320, 50);

        add_btn.setBounds(270, 400, 70, 30);
        update.setBounds(360, 400, 70, 30);
        back.setBounds(320, 450, 70, 30);

        logo.setBackground(new Color(152,251,152));
        slogan.setBackground(new Color(152,251,152));
        add_btn.setBackground(new Color(173, 216, 230));
        update.setBackground(new Color(173, 216, 230));
        back.setBackground(Color.LIGHT_GRAY);

        Font font = new Font("Arial", Font.BOLD, 30);
        Font sfont = new Font("Times New Roman",Font.ITALIC,20);
        Font bfont = new Font("Verdana",Font.BOLD,12);
        logo.setFont(font);
        slogan.setFont(sfont);
        add_btn.setFont(bfont);
        add_btn.setForeground(Color.WHITE);
        update.setFont(bfont);
        update.setForeground(Color.WHITE);
        back.setFont(bfont);

        add_btn.addActionListener(this);
        update.addActionListener(this);
        back.addActionListener(this);

        add(logo);
        add(slogan);
        add(add_btn);
        add(update);
        add(back);

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        setSize(800, 600);
        setTitle("CRUD Operations");
        setLayout(null);
        setLocationRelativeTo(null);
        setVisible(true);
        setBackground(new Color(152,251,152));

    }

    public void actionPerformed(ActionEvent ae) {
        if (ae.getActionCommand().equals("Add")) {
            Label l1 = new Label("Customer ID:");
            Label l2 = new Label("Feedback:");
            TextField id = new TextField();
            TextField feedback = new TextField();
            Button proceed = new Button("Proceed");

            l1.setBounds(50, 250, 100, 20);
            l2.setBounds(50, 280, 100, 20);
            id.setBounds(160, 250, 150, 20);
            feedback.setBounds(160, 280, 150, 20);
            proceed.setBounds(50, 340, 70, 20);

            proceed.setBackground(new Color(173, 216, 230));
            proceed.setFont(new Font("Verdana",Font.BOLD,12));
            proceed.setForeground(Color.WHITE);

            ProceedActionListener proceedListener = new ProceedActionListener(id, feedback);
            proceed.addActionListener(proceedListener);

            add(l1);
            add(id);
            add(l2);
            add(feedback);
            add(proceed);

            repaint();
        } else if(ae.getActionCommand().equals("Update")) {
            Label l1 = new Label("Customer ID:");
            Label l2 = new Label("Feedback:");
            TextField id = new TextField();
            TextField feedback = new TextField();
            Button update = new Button("Update");

            l1.setBounds(50, 250, 100, 20);
            l2.setBounds(50, 280, 100, 20);
            id.setBounds(160, 250, 150, 20);
            feedback.setBounds(160, 280, 150, 20);
            update.setBounds(50, 340, 70, 20);


            update.setBackground(new Color(173, 216, 230));
            update.setFont(new Font("Verdana",Font.BOLD,12));
            update.setForeground(Color.WHITE);


            UpdateActionListener updateListener = new UpdateActionListener(id, feedback);
            update.addActionListener(updateListener);

            add(l1);
            add(id);
            add(l2);
            add(feedback);
            add(update);

            repaint();

        } else if (ae.getActionCommand().equals("Back")) {
            setVisible(false);
            dispose();
        }
    }

    void perform(String id, String feedback) {
        int id_int = Integer.parseInt(id);
        int count = 0;

        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            Connection con = DriverManager.getConnection("jdbc:oracle:thin:@url", "user", "password");
            String select = "SELECT * FROM feedback";
            PreparedStatement statement = con.prepareStatement(select);
            ResultSet res = statement.executeQuery();

            while(res.next()){
                new_feed_id = res.getInt("feedback_id");
            }
            new_feed_id = new_feed_id + 1;

            String check = "SELECT COUNT(customer_id) AS count FROM personal WHERE customer_id = ?";
            PreparedStatement chstmt = con.prepareStatement(check);
            chstmt.setInt(1,id_int);
            ResultSet rs = chstmt.executeQuery();

            while(rs.next()){
                count = rs.getInt("count");
            }

            if(count == 0){
                System.out.println("Invalid User");
            } else {

                String query = "INSERT INTO feedback VALUES (?, ?)";
                String update = "UPDATE personal SET feedback_id = ? WHERE customer_id = ?";
                PreparedStatement stmt = con.prepareStatement(query);
                PreparedStatement stmt2 = con.prepareStatement(update);
                stmt.setInt(1, new_feed_id);
                stmt.setString(2, feedback);
                stmt2.setInt(1,new_feed_id);
                stmt2.setInt(2,id_int);

                int rows = stmt.executeUpdate();
                int rows2 = stmt2.executeUpdate();

                stmt.close();
                stmt2.close();
                con.close();
            }
            
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void update(String cid, String feedback) {
        int id_int = Integer.parseInt(cid);
        int feed_id = 0;

        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            Connection con = DriverManager.getConnection("jdbc:oracle:thin:@url", "user", "password");
            ResultSet rs;

            String select = "SELECT feedback_id FROM personal WHERE customer_id = ?";
            PreparedStatement stm = con.prepareStatement(select);

            stm.setInt(1,id_int);

            rs = stm.executeQuery();
            
            while(rs.next()){
                feed_id = rs.getInt("feedback_id");
            }

            if(feed_id == 0){
                System.out.println("Invalid user id");
            } else{
                String query = "UPDATE feedback SET feedback_text = ? WHERE feedback_id =?";
                PreparedStatement stmt = con.prepareStatement(query);

                stmt.setInt(2, feed_id);
                stmt.setString(1, feedback);

                int rows = stmt.executeUpdate();

                stmt.close();
                con.close();
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    class ProceedActionListener implements ActionListener {
        private TextField idField;
        private TextField feedbackField;

        public ProceedActionListener(TextField idField, TextField feedbackField) {
            this.idField = idField;
            this.feedbackField = feedbackField;
        }

        public void actionPerformed(ActionEvent ae) {
            String id = idField.getText();
            String feedback = feedbackField.getText();

            //System.out.println(id + " " + feedback);
            perform(id, feedback);
        }
    }

    class UpdateActionListener implements ActionListener {
        private TextField idField;
        private TextField feedbackField;

        public UpdateActionListener(TextField idField, TextField feedbackField) {
            this.idField = idField;
            this.feedbackField = feedbackField;
        }

        public void actionPerformed(ActionEvent ae) {
            String id = idField.getText();
            String feedback = feedbackField.getText();

            update(id, feedback);
        }
    }
}

class Home extends Frame implements ActionListener{
    Label logo, slogan;
    Button crud,visualize,close,exit;
    Home(){
    
        logo = new Label("Spotify Inc.");
        slogan = new Label("Customer Relationship Diagnosis");

        crud = new Button("Modify Feedbacks");
        visualize = new Button("Visualize Feedbacks");
        exit = new Button("Exit");

        logo.setBounds(230,100,300,50);
        slogan.setBounds(150,120,400,100);

        crud.setBounds(50,300,200,50);
        visualize.setBounds(300,300,200,50);
        exit.setBounds(210,400,100,50);

        logo.setBackground(new Color(152,251,152));
        slogan.setBackground(new Color(152,251,152));
        crud.setBackground(new Color(173, 216, 230));
        visualize.setBackground(new Color(173, 216, 230));
        exit.setBackground(Color.LIGHT_GRAY);

        Font font = new Font("Arial", Font.BOLD, 30);
        Font sfont = new Font("Times New Roman",Font.ITALIC,20);
        Font bfont = new Font("Verdana",Font.BOLD,12);
        logo.setFont(font);
        slogan.setFont(sfont);
        crud.setFont(bfont);
        crud.setForeground(Color.WHITE);
        visualize.setFont(bfont);
        visualize.setForeground(Color.WHITE);
        exit.setFont(bfont);

        crud.addActionListener(this);
        visualize.addActionListener(this);
        exit.addActionListener(this);
        
        add(logo);
        add(slogan);

        add(crud);
        add(visualize);
        add(exit);

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        setSize(600,600);
        setTitle("Spotify Inc.");
        setLayout(null);
        setLocationRelativeTo(null);
        setVisible(true);
        setBackground(new Color(152,251,152));

    }

    public void actionPerformed(ActionEvent ae){
        if(ae.getActionCommand().equals("Modify Feedbacks")){
            Connect c1 = new Connect();
        } else if(ae.getActionCommand().equals("Visualize Feedbacks")) {
            Visual v1 = new Visual();
            //v1.plot();
        } else if(ae.getActionCommand().equals("Exit")){
            setVisible(false);
            dispose();
        }
    }
}

class Login extends Frame implements ActionListener {
    Label logo,slogan,n,p,msg;
    TextField name,pwd;
    Button submit;
    Login() {
        logo = new Label("Spotify Inc.");
        slogan = new Label("Customer Relationship Diagnosis");
        n = new Label("Enter name:");
        p = new Label("Enter password");
        name = new TextField();
        pwd = new TextField();
        msg = new Label();
        submit = new Button("Submit");

        logo.setBounds(200, 100, 300, 50);
        slogan.setBounds(150, 170, 400, 50);
        n.setBounds(50, 250, 100, 20);
        name.setBounds(170, 250, 200, 20);
        p.setBounds(50, 290, 100, 20);
        pwd.setBounds(170, 290, 200, 20);
        msg.setBounds(170,320,200,40);
        submit.setBounds(180, 360, 70, 30);

        logo.setBackground(new Color(152, 251, 152));
        slogan.setBackground(new Color(152, 251, 152));
        n.setBackground(new Color(152, 251, 152));
        p.setBackground(new Color(152, 251, 152));
        msg.setBackground(new Color(152,251,152));

        Font font = new Font("Arial", Font.BOLD, 30);
        Font sfont = new Font("Times New Roman", Font.ITALIC, 20);
        logo.setFont(font);
        slogan.setFont(sfont);
        msg.setFont(sfont);


        pwd.setEchoChar('*');
        submit.addActionListener(this);

        add(logo);
        add(slogan);
        add(n);
        add(name);
        add(p);
        add(pwd);
        add(msg);
        add(submit);

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        setSize(600, 500);
        setTitle("Login");
        setLayout(null);
        setLocationRelativeTo(null); 
        setVisible(true);
        setBackground(new Color(152, 251, 152));
    }

    public void actionPerformed(ActionEvent ae) {
        int present = 0;
        String pass=" ";
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            Connection con = DriverManager.getConnection("jdbc:oracle:thin:@url", "user", "password");
            ResultSet rs;

            String select = "SELECT COUNT(name) as count FROM admin WHERE name = ?";
            PreparedStatement stm = con.prepareStatement(select);
            stm.setString(1,name.getText());
            rs = stm.executeQuery();

            while(rs.next()) {
                present = rs.getInt("count");
            }
            if(present == 0) {
                msg.setText("Invalid User");
            } else {
                String cpwd = "SELECT pass FROM admin WHERE name = ?";
                PreparedStatement stmt = con.prepareStatement(cpwd);
                stmt.setString(1,name.getText());
                rs = stmt.executeQuery();
                while(rs.next()) {
                    pass = rs.getString(1);
                }
                if(pass.equals(pwd.getText())) {
                    Home h1 = new Home();
                    setVisible(false);
                    dispose();
                } else {
                    msg.setText("Invalid Password");
                }
            }
    } catch(Exception e) {
        System.out.println(e);
    }
}
}

class crd {
    public static void main(String[] args) {
        Login l1 = new Login();
    }
}
