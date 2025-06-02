import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.Queue;

public class PatientManagementGUI extends JFrame {
    private JLabel titleLabel;
    private JLabel patientInfoLabel;
    private JLabel bedAllocationLabel;
    private JLabel availableBedsLabel;
    private JTextField availableBedsTextField;
    private JTextArea outputTextArea;
    private JButton inputButton;
    private JButton allocateButton;

    private Queue<Integer> highPriorityQueue;
    private Queue<Integer> mediumPriorityQueue;
    private Queue<Integer> lowPriorityQueue;

    private String[] highprn;
    private String[] medprn;
    private String[] lowprn;

    private int[] highpr;
    private int[] medpr;
    private int[] lowpr;

    private int[] higha;
    private int[] meda;
    private int[] lowa;

    private int bedsAllocated;
    private int totalPatients; 
    private int currentIndex; // Keeps track of the current patient being input

    public PatientManagementGUI(int n) {
        setTitle("Patient Management System");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        highprn = new String[n];
        medprn = new String[n];
        lowprn = new String[n];

       highpr = new int[n];
       medpr = new int[n];
       lowpr = new int[n];

       higha = new int[n];
       meda = new int[n];
       lowa = new int[n];

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout());
        titleLabel = new JLabel("Patient Management System");
        topPanel.add(titleLabel);
        add(topPanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new GridLayout(2,1));

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new FlowLayout());
        patientInfoLabel = new JLabel("Patient Information: ");
        inputButton = new JButton("Input Data");
        inputPanel.add(patientInfoLabel);
        inputPanel.add(inputButton);

        JPanel allocationPanel = new JPanel();
        allocationPanel.setLayout(new FlowLayout());
        bedAllocationLabel = new JLabel("Bed Allocation: ");
        availableBedsLabel = new JLabel("Available Beds:");
        availableBedsTextField = new JTextField(5);
        allocateButton = new JButton("Allocate Beds");
        allocationPanel.add(bedAllocationLabel);
        allocationPanel.add(availableBedsLabel);
        allocationPanel.add(availableBedsTextField);
        allocationPanel.add(allocateButton);

        centerPanel.add(inputPanel);
        centerPanel.add(allocationPanel);
        add(centerPanel, BorderLayout.CENTER);
        
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new GridLayout(2,1));
        outputTextArea = new JTextArea();
        outputTextArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(outputTextArea);
        bottomPanel.add(scrollPane);
        add(bottomPanel, BorderLayout.SOUTH);
        

        highPriorityQueue = new LinkedList<>();
        mediumPriorityQueue = new LinkedList<>();
        lowPriorityQueue = new LinkedList<>();

        currentIndex = 0;
        totalPatients = n;
        inputButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Implement code to collect patient information from the user
                collectPatientInformation(n);
            }
        });

        allocateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Implement code to allocate beds based on available beds and patient priority
                sortPatientData();
                allocateBeds();
            }
        });
    }

    private void collectPatientInformation(int n) {
        if (currentIndex >= n) {
            JOptionPane.showMessageDialog(this, "All patient information has been collected.");
            sortPatientData();
            return;
        }

        String[] priorities = { "High Priority", "Medium Priority", "Low Priority" };
        int priority = JOptionPane.showOptionDialog(
                this,
                "Select priority for Patient " + (currentIndex + 1),
                "Priority Selection",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                priorities,
                priorities[0]
        ) + 1; // Adjust the priority value to start from 1

        String name = JOptionPane.showInputDialog(this, "Enter patient Name:");
        int executionTime = Integer.parseInt(JOptionPane.showInputDialog(this, "Enter arrival time of patient:"));
        int age = Integer.parseInt(JOptionPane.showInputDialog(this, "Enter age of patient:"));

        if (priority < 1 || priority > 3) {
            JOptionPane.showMessageDialog(this, "Invalid priority! Please enter a valid priority (1 to 3).");
            return;
        }

        if (executionTime < 0 || executionTime > 24) {
            JOptionPane.showMessageDialog(this, "Invalid arrival time! Please enter a valid value.");
            return;
        }

        if (age < 0) {
            JOptionPane.showMessageDialog(this, "Invalid age! Please enter a non-negative value.");
            return;
        }

        // Store the collected information in respective arrays
        if (priority == 1) {
            highprn[currentIndex] = name;
            highpr[currentIndex] = executionTime;
            higha[currentIndex] = age;
        } else if (priority == 2) {
            medprn[currentIndex] = name;
            medpr[currentIndex] = executionTime;
            meda[currentIndex] = age;
        } else {
            lowprn[currentIndex] = name;
            lowpr[currentIndex] = executionTime;
            lowa[currentIndex] = age;
        }

        currentIndex++;

        // Display patient information in the outputTextArea
        outputTextArea.append("Patient " + currentIndex + " added: Priority: " + priority + ", Name: " + name + ", Arrival Time: " + executionTime + ", Age: " + age + "\n");
    }

    private void sortPatientData(){
        int temp, i, j;
        String swap;
        for (i = 0; i < highpr.length; i++) {
            for (j = 1; j < (highpr.length - i); j++) {
                if((highpr[j - 1] > highpr[j])||((highpr[j - 1] == highpr[j])&&(higha[j - 1] < higha[j]))) {
                    temp = highpr[j - 1];
                    highpr[j - 1] = highpr[j];
                    highpr[j] = temp;

                    swap = highprn[j - 1];
                    highprn[j - 1] = highprn[j];
                    highprn[j] = swap;
 
                    temp = higha[j - 1];
                    higha[j - 1] = higha[j];
                    higha[j] = temp;

                }

            }
        }
        for (i = 0; i < medpr.length; i++) {
            for (j = 1; j < (medpr.length - i); j++) {
                if ((medpr[j - 1] > medpr[j])||((medpr[j - 1] == medpr[j])&&(meda[j - 1] < meda[j]))) {
                    temp = medpr[j - 1];
                    medpr[j - 1] = medpr[j];
                    medpr[j] = temp;

                    swap = medprn[j - 1];
                    medprn[j - 1] = medprn[j];
                    medprn[j] = swap;

                    temp = meda[j - 1];
                    meda[j - 1] = meda[j];
                    meda[j] = temp;

                }
                
               }
            }
        for (i = 0; i < lowpr.length; i++) {
            for (j = 1; j < (lowpr.length - i); j++) {
                if ((lowpr[j - 1] > lowpr[j]) ||((lowpr[j - 1] == lowpr[j])&&(lowa[j - 1] < lowa[j]))){
                    temp = lowpr[j - 1];
                    lowpr[j - 1] = lowpr[j];
                    lowpr[j] = temp;

                    swap = lowprn[j - 1];
                    lowprn[j - 1] = lowprn[j];
                    lowprn[j] = swap;

                    temp = lowa[j - 1];
                    lowa[j - 1] = lowa[j];
                    lowa[j] = temp;
                }
          }
        }
    }

    private void allocateBeds() {
        // Implement code to allocate beds based on available beds and patient priority
        // Update bedsAllocated and display bed allocation information in outputTextArea

        // Clear the queues
        highPriorityQueue.clear();
        mediumPriorityQueue.clear();
        lowPriorityQueue.clear();

        // Populate the queues with patients based on priority
        for (int i = 0; i < highpr.length; i++) {
            if (highprn[i] != null) {
                highPriorityQueue.offer(i);
            }
        }
        for (int i = 0; i < medpr.length; i++) {
            if (medprn[i] != null) {
                mediumPriorityQueue.offer(i);
            }
        }
        for (int i = 0; i < lowpr.length; i++) {
            if (lowprn[i] != null) {
                lowPriorityQueue.offer(i);
            }
        }

        int availableBeds;
        try {
            availableBeds = Integer.parseInt(availableBedsTextField.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid input for available beds. Please enter a valid number.");
            return;
        }

        bedsAllocated = 0;
    while (bedsAllocated < availableBeds) {
        if (!highPriorityQueue.isEmpty()) {
            int patientIndex = highPriorityQueue.poll();
            outputTextArea.append("Allocated Bed to: Name: " + highprn[patientIndex] + ", Priority: High, Age: " + higha[patientIndex] + "\n");
            bedsAllocated++;
        } else if (!mediumPriorityQueue.isEmpty()) {
            int patientIndex = mediumPriorityQueue.poll();
            outputTextArea.append("Allocated Bed to: Name: " + medprn[patientIndex] + ", Priority: Medium, Age: " + meda[patientIndex] + "\n");
            bedsAllocated++;
        } else if (!lowPriorityQueue.isEmpty()) {
            int patientIndex = lowPriorityQueue.poll();
            outputTextArea.append("Allocated Bed to: Name: " + lowprn[patientIndex] + ", Priority: Low, Age: " + lowa[patientIndex] + "\n");
            bedsAllocated++;
        } else {
            break; // No more patients to allocate
        }
    }

    int patientsWaiting = highPriorityQueue.size() + mediumPriorityQueue.size() + lowPriorityQueue.size();
    if (patientsWaiting > 0) {
        outputTextArea.append("The rest of the " + patientsWaiting + " patients have to wait.\n");
    } else {
        outputTextArea.append("All patients have been allocated beds.\n");
    }
}



    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                int n = Integer.parseInt(JOptionPane.showInputDialog("Enter number of patients:"));
                PatientManagementGUI gui = new PatientManagementGUI(n);
                gui.setVisible(true);
            }
        });
    }
}