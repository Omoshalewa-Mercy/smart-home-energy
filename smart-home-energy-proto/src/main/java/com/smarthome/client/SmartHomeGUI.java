package com.smarthome.client;

import com.smarthome.generated.*;
import com.smarthome.util.AuthInterceptor;
import com.smarthome.util.ServiceDiscoveryUtil;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.Metadata;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.MetadataUtils;
import io.grpc.stub.StreamObserver;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class SmartHomeGUI extends JFrame {
    private static final Logger logger = Logger.getLogger(SmartHomeGUI.class.getName());

    private static final Color BG_DARK      = new Color(18, 18, 18);
    private static final Color BG_CARD      = new Color(30, 30, 30);
    private static final Color BG_INPUT     = new Color(45, 45, 45);
    private static final Color ACCENT_GREEN  = new Color(46, 204, 113);
    private static final Color ACCENT_YELLOW = new Color(241, 196, 15);
    private static final Color ACCENT_BLUE   = new Color(52, 152, 219);
    private static final Color ACCENT_RED    = new Color(231, 76, 60);
    private static final Color TEXT_PRIMARY  = new Color(240, 240, 240);
    private static final Color TEXT_MUTED    = new Color(150, 150, 150);

    private ManagedChannel energyChannel, solarChannel, applianceChannel;
    private EnergyMonitoringServiceGrpc.EnergyMonitoringServiceBlockingStub energyStub;
    private EnergyMonitoringServiceGrpc.EnergyMonitoringServiceStub energyAsyncStub;
    private SolarEnergyServiceGrpc.SolarEnergyServiceStub solarAsyncStub;
    private SolarEnergyServiceGrpc.SolarEnergyServiceBlockingStub solarBlockingStub;
    private ApplianceControlServiceGrpc.ApplianceControlServiceStub applianceAsyncStub;
    private ApplianceControlServiceGrpc.ApplianceControlServiceBlockingStub applianceBlockingStub;

    private JLabel energyStatusLabel, solarStatusLabel, applianceStatusLabel;
    private JTextArea energyLogArea, solarLogArea, applianceLogArea;
    private StreamObserver<ApplianceCommand> applianceCommandObserver;

    public SmartHomeGUI() {
        setTitle("Smart Home Energy Management System — SDG 7");
        setSize(1000, 720);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(BG_DARK);
        setLayout(new BorderLayout());
        add(buildHeader(), BorderLayout.NORTH);
        add(buildTabbedPane(), BorderLayout.CENTER);
        add(buildFooter(), BorderLayout.SOUTH);
        discoverAndConnect();
    }

    private void discoverAndConnect() {
        SwingWorker<Map<String, String>, Void> worker = new SwingWorker<>() {
            @Override protected Map<String, String> doInBackground() throws Exception {
                log(energyLogArea, "Discovering services via jmDNS...");
                log(solarLogArea,  "Discovering services via jmDNS...");
                log(applianceLogArea, "Discovering services via jmDNS...");
                return new ServiceDiscoveryUtil().discoverServices();
            }
            @Override protected void done() {
                try {
                    Map<String, String> services = get();
                    Metadata headers = new Metadata();
                    headers.put(AuthInterceptor.API_KEY_HEADER, "smarthome-secret-2026");

                    String energyAddr = services.getOrDefault("EnergyMonitoringService", "localhost:50051");
                    String[] ep = energyAddr.split(":");
                    energyChannel = ManagedChannelBuilder
                        .forAddress(ep[0], Integer.parseInt(ep[1]))
                        .usePlaintext()
                        .build();

                    energyStub = EnergyMonitoringServiceGrpc
                                .newBlockingStub(energyChannel)
                                .withInterceptors(
                                        MetadataUtils.newAttachHeadersInterceptor(headers)
                        );
                    energyAsyncStub = EnergyMonitoringServiceGrpc
                                .newStub(energyChannel)
                                .withInterceptors(
                                        MetadataUtils.newAttachHeadersInterceptor(headers)
                        );
                    updateStatus(energyStatusLabel, "Connected — " + energyAddr, ACCENT_GREEN);
                    log(energyLogArea, "Connected to Energy Monitoring Service at " + energyAddr);

                    String solarAddr = services.getOrDefault("SolarEnergyService", "localhost:50052");
                    String[] sp = solarAddr.split(":");
                    solarChannel = ManagedChannelBuilder
                                .forAddress(sp[0], Integer.parseInt(sp[1]))
                                .usePlaintext()
                                .build();

                    solarAsyncStub = SolarEnergyServiceGrpc
                                .newStub(solarChannel)
                                .withInterceptors(
                                        MetadataUtils.newAttachHeadersInterceptor(headers)
                                );
                    solarBlockingStub = SolarEnergyServiceGrpc
                                .newBlockingStub(solarChannel)
                                .withInterceptors(
                                        MetadataUtils.newAttachHeadersInterceptor(headers)
                                );
                    updateStatus(solarStatusLabel, "Connected — " + solarAddr, ACCENT_GREEN);
                    log(solarLogArea, "Connected to Solar Energy Service at " + solarAddr);

                    String applianceAddr = services.getOrDefault("ApplianceControlService", "localhost:50053");
                    String[] ap = applianceAddr.split(":");
                    applianceChannel = ManagedChannelBuilder
                                .forAddress(ap[0], Integer.parseInt(ap[1]))
                                .usePlaintext()
                                .build();

                    applianceAsyncStub = ApplianceControlServiceGrpc
                                .newStub(applianceChannel)
                                .withInterceptors(
                                        MetadataUtils.newAttachHeadersInterceptor(headers)
                                );
                    applianceBlockingStub = ApplianceControlServiceGrpc
                                .newBlockingStub(applianceChannel)
                                .withInterceptors(
                                        MetadataUtils.newAttachHeadersInterceptor(headers)
                                );
                    updateStatus(applianceStatusLabel, "Connected — " + applianceAddr, ACCENT_GREEN);
                    log(applianceLogArea, "Connected to Appliance Control Service at " + applianceAddr);
                } catch (Exception e) {
                    log(energyLogArea, "Discovery error: " + e.getMessage());
                    log(solarLogArea, "Discovery error: " + e.getMessage());
                    log(applianceLogArea, "Discovery error: " + e.getMessage());
                }
            }
        };
        worker.execute();
    }

    private JPanel buildHeader() {
        JPanel h = new JPanel(new BorderLayout());
        h.setBackground(BG_CARD);
        h.setBorder(new EmptyBorder(14, 20, 14, 20));
        JLabel title = new JLabel("Smart Home Energy Management System");
        title.setFont(new Font("Arial", Font.BOLD, 20));
        title.setForeground(ACCENT_GREEN);
        JLabel sub = new JLabel("SDG 7: Affordable and Clean Energy  |  National College of Ireland");
        sub.setFont(new Font("Arial", Font.PLAIN, 12));
        sub.setForeground(TEXT_MUTED);
        JPanel tp = new JPanel(new GridLayout(2, 1));
        tp.setBackground(BG_CARD);
        tp.add(title); tp.add(sub);
        h.add(tp, BorderLayout.WEST);
        return h;
    }

    private JPanel buildFooter() {
        JPanel f = new JPanel(new FlowLayout(FlowLayout.LEFT));
        f.setBackground(BG_CARD);
        f.setBorder(new EmptyBorder(6, 16, 6, 16));
        JLabel lbl = new JLabel("gRPC communication | jmDNS discovery | API key authentication");
        lbl.setFont(new Font("Arial", Font.PLAIN, 11));
        lbl.setForeground(TEXT_MUTED);
        f.add(lbl);
        return f;
    }

    private JTabbedPane buildTabbedPane() {
        JTabbedPane tabs = new JTabbedPane();
        tabs.setBackground(BG_DARK);
        tabs.setForeground(TEXT_PRIMARY);
        tabs.setFont(new Font("Arial", Font.BOLD, 13));
        tabs.addTab("Energy Monitoring", buildEnergyTab());
        tabs.addTab("Solar Energy",       buildSolarTab());
        tabs.addTab("Appliance Control",  buildApplianceTab());
        return tabs;
    }

    private JPanel buildEnergyTab() {
        JPanel tab = new JPanel(new BorderLayout(10, 10));
        tab.setBackground(BG_DARK);
        tab.setBorder(new EmptyBorder(14, 14, 14, 14));
        energyStatusLabel = statusLabel("Discovering...");
        tab.add(wrapStatus(energyStatusLabel), BorderLayout.NORTH);

        JPanel controls = new JPanel(new GridLayout(1, 3, 10, 0));
        controls.setBackground(BG_DARK);

        JTextField hhId = styledField("HOME_001");
        JTextField kWh  = styledField("1.25");
        JButton rec = styledButton("Record Usage", ACCENT_GREEN);
        rec.addActionListener(e -> {
            try {
                RecordUsageRequest req = RecordUsageRequest.newBuilder().setHouseholdId(hhId.getText().trim())
                        .setEnergyUsed(Double.parseDouble(kWh.getText().trim())).setTimestamp(java.time.LocalDateTime.now().toString()).build();
                RecordUsageResponse res = energyStub.withDeadlineAfter(5, TimeUnit.SECONDS).recordEnergyUsage(req);
                log(energyLogArea, "RecordEnergyUsage -> " + res.getMessage());
            } catch (StatusRuntimeException ex) { log(energyLogArea, "Error: " + ex.getStatus().getDescription()); }
              catch (Exception ex) { log(energyLogArea, "Error: " + ex.getMessage()); }
        });
        controls.add(buildCard("Record Energy Usage", new String[]{"Household ID", "Energy Used (kWh)"}, new JComponent[]{hhId, kWh}, rec, ACCENT_GREEN));

        JTextField getHh = styledField("HOME_001");
        JButton getLat = styledButton("Get Latest Reading", ACCENT_BLUE);
        getLat.addActionListener(e -> {
            try {
                LatestReadingResponse res = energyStub.withDeadlineAfter(5, TimeUnit.SECONDS)
                        .getLatestReading(LatestReadingRequest.newBuilder().setHouseholdId(getHh.getText().trim()).build());
                log(energyLogArea, "GetLatestReading -> " + res.getEnergyUsed() + " kWh at " + res.getTimestamp());
            } catch (StatusRuntimeException ex) { log(energyLogArea, "Error: " + ex.getStatus().getDescription()); }
        });
        controls.add(buildCard("Get Latest Reading", new String[]{"Household ID"}, new JComponent[]{getHh}, getLat, ACCENT_BLUE));

        JTextField stHh  = styledField("HOME_001");
        JTextField stInt = styledField("1");
        JButton strBtn = styledButton("Start Live Stream", ACCENT_YELLOW);
        strBtn.addActionListener(e -> {
            log(energyLogArea, "StreamLiveReadings started...");
            StreamRequest req = StreamRequest.newBuilder().setHouseholdId(stHh.getText().trim())
                    .setIntervalSeconds(Integer.parseInt(stInt.getText().trim())).build();
            energyAsyncStub.streamLiveReadings(req, new StreamObserver<EnergyReading>() {
                @Override public void onNext(EnergyReading r) { log(energyLogArea, r.getTimestamp() + " | " + r.getEnergyUsed() + " kWh | " + r.getStatus()); }
                @Override public void onError(Throwable t) { log(energyLogArea, "Stream error: " + t.getMessage()); }
                @Override public void onCompleted() { log(energyLogArea, "Stream completed"); }
            });
        });
        controls.add(buildCard("Stream Live Readings", new String[]{"Household ID", "Interval (seconds)"}, new JComponent[]{stHh, stInt}, strBtn, ACCENT_YELLOW));

        tab.add(controls, BorderLayout.CENTER);
        energyLogArea = buildLogArea();
        tab.add(wrapLog(energyLogArea), BorderLayout.SOUTH);
        return tab;
    }

    private JPanel buildSolarTab() {
        JPanel tab = new JPanel(new BorderLayout(10, 10));
        tab.setBackground(BG_DARK);
        tab.setBorder(new EmptyBorder(14, 14, 14, 14));
        solarStatusLabel = statusLabel("Discovering...");
        tab.add(wrapStatus(solarStatusLabel), BorderLayout.NORTH);

        JPanel controls = new JPanel(new GridLayout(1, 2, 10, 0));
        controls.setBackground(BG_DARK);

        JTextField panelId = styledField("PANEL_001");
        JTextField numR    = styledField("5");
        JButton upBtn = styledButton("Upload Solar Readings", ACCENT_YELLOW);
        upBtn.addActionListener(e -> {
            log(solarLogArea, "Starting UploadSolarReadings (client streaming)...");
            int count; try { count = Integer.parseInt(numR.getText().trim()); } catch (NumberFormatException ex) { log(solarLogArea, "Invalid count"); return; }
            String pid = panelId.getText().trim();
            StreamObserver<SolarReading> requestObserver = solarAsyncStub.uploadSolarReadings(new StreamObserver<SolarSummary>() {
                @Override public void onNext(SolarSummary s) {
                    log(solarLogArea, "Upload complete!");
                    log(solarLogArea, "  Total readings : " + s.getTotalReadings());
                    log(solarLogArea, "  Total generated: " + s.getTotalGeneratedKwh() + " kWh");
                    log(solarLogArea, "  Peak generation: " + s.getPeakGenerationKwh() + " kWh");
                    log(solarLogArea, "  Avg irradiance : " + s.getAverageIrradiance() + " W/m2");
                }
                @Override public void onError(Throwable t) { log(solarLogArea, "Upload error: " + t.getMessage()); }
                @Override public void onCompleted() { log(solarLogArea, "Server acknowledged all readings"); }
            });
            new Thread(() -> {
                try {
                    for (int i = 1; i <= count; i++) {
                        double power = Math.round((0.5 + Math.random() * 3.5) * 100.0) / 100.0;
                        double irr   = Math.round((200 + Math.random() * 800) * 10.0) / 10.0;
                        String ts    = java.time.LocalDateTime.now().minusHours(count - i).toString();
                        requestObserver.onNext(SolarReading.newBuilder().setPanelId(pid).setTimestamp(ts).setGeneratedPower(power).setIrradiance(irr).build());
                        log(solarLogArea, "Sent reading " + i + ": " + power + " kWh | " + irr + " W/m2");
                        Thread.sleep(300);
                    }
                    requestObserver.onCompleted();
                } catch (Exception ex) { requestObserver.onError(ex); }
            }).start();
        });
        controls.add(buildCard("Upload Solar Readings", new String[]{"Panel ID", "Number of Readings"}, new JComponent[]{panelId, numR}, upBtn, ACCENT_YELLOW));

        JTextField sumPanel = styledField("PANEL_001");
        JTextField sumDate  = styledField(java.time.LocalDate.now().toString());
        JButton sumBtn = styledButton("Get Daily Summary", ACCENT_BLUE);
        sumBtn.addActionListener(e -> {
            try {
                DailySummaryResponse res = solarBlockingStub.withDeadlineAfter(5, TimeUnit.SECONDS)
                        .getDailySummary(DailySummaryRequest.newBuilder().setPanelId(sumPanel.getText().trim()).setDate(sumDate.getText().trim()).build());
                log(solarLogArea, "GetDailySummary ->");
                log(solarLogArea, "  Total generated: " + res.getTotalGeneratedKwh() + " kWh");
                log(solarLogArea, "  Peak generation: " + res.getPeakGenerationKwh() + " kWh");
                log(solarLogArea, "  Start time     : " + res.getStartTime());
                log(solarLogArea, "  End time       : " + res.getEndTime());
            } catch (StatusRuntimeException ex) { log(solarLogArea, "Error: " + ex.getStatus().getDescription()); }
        });
        controls.add(buildCard("Get Daily Summary", new String[]{"Panel ID", "Date (YYYY-MM-DD)"}, new JComponent[]{sumPanel, sumDate}, sumBtn, ACCENT_BLUE));

        tab.add(controls, BorderLayout.CENTER);
        solarLogArea = buildLogArea();
        tab.add(wrapLog(solarLogArea), BorderLayout.SOUTH);
        return tab;
    }

    private JPanel buildApplianceTab() {
        JPanel tab = new JPanel(new BorderLayout(10, 10));
        tab.setBackground(BG_DARK);
        tab.setBorder(new EmptyBorder(14, 14, 14, 14));
        applianceStatusLabel = statusLabel("Discovering...");
        tab.add(wrapStatus(applianceStatusLabel), BorderLayout.NORTH);

        JPanel controls = new JPanel(new GridLayout(1, 3, 10, 0));
        controls.setBackground(BG_DARK);

        // Card 1: Open stream
        JButton openBtn = styledButton("Open Control Stream", ACCENT_GREEN);
        openBtn.addActionListener(e -> {
            if (applianceCommandObserver != null) { log(applianceLogArea, "Stream already open"); return; }
            applianceCommandObserver = applianceAsyncStub.controlAppliance(new StreamObserver<ApplianceStatus>() {
                @Override public void onNext(ApplianceStatus s) {
                    log(applianceLogArea, "Status: " + s.getApplianceName() + " | " + s.getStatus() + " | " + s.getPowerConsumption() + "W | " + s.getMessage());
                }
                @Override public void onError(Throwable t) { log(applianceLogArea, "Stream error: " + t.getMessage()); applianceCommandObserver = null; }
                @Override public void onCompleted() { log(applianceLogArea, "Control stream closed"); applianceCommandObserver = null; }
            });
            log(applianceLogArea, "Bidirectional control stream opened");
        });
        controls.add(buildCard("Open Control Stream", new String[]{}, new JComponent[]{}, openBtn, ACCENT_GREEN));

        // Card 2: Send command
        JTextField appId   = styledField("AC_LIVING_ROOM_01");
        JTextField appName = styledField("Living Room Air Conditioner");
        JTextField room    = styledField("Living Room");
        JComboBox<String> actionCombo = new JComboBox<>(new String[]{"ON", "OFF", "ECO"});
        actionCombo.setBackground(BG_INPUT); actionCombo.setForeground(TEXT_PRIMARY); actionCombo.setFont(new Font("Arial", Font.PLAIN, 13));
        JButton sendBtn = styledButton("Send Command", ACCENT_BLUE);
        sendBtn.addActionListener(e -> {
            if (applianceCommandObserver == null) { log(applianceLogArea, "Open the control stream first"); return; }
            ApplianceCommand cmd = ApplianceCommand.newBuilder()
                    .setApplianceId(appId.getText().trim()).setApplianceName(appName.getText().trim())
                    .setAction((String) actionCombo.getSelectedItem()).setRoomLocation(room.getText().trim()).build();
            applianceCommandObserver.onNext(cmd);
            log(applianceLogArea, "Command sent: " + cmd.getAction() + " -> " + cmd.getApplianceName());
        });
        JPanel cmdCard = new JPanel(new BorderLayout(0, 8));
        cmdCard.setBackground(BG_CARD);
        cmdCard.setBorder(BorderFactory.createCompoundBorder(new LineBorder(ACCENT_BLUE, 1, true), new EmptyBorder(12, 12, 12, 12)));
        JLabel cmdTitle = new JLabel("Send Appliance Command"); cmdTitle.setFont(new Font("Arial", Font.BOLD, 13)); cmdTitle.setForeground(ACCENT_BLUE);
        JPanel cmdFields = new JPanel(new GridLayout(8, 1, 0, 6)); cmdFields.setBackground(BG_CARD);
        cmdFields.add(fieldLabel("Appliance ID")); cmdFields.add(appId);
        cmdFields.add(fieldLabel("Appliance Name")); cmdFields.add(appName);
        cmdFields.add(fieldLabel("Room")); cmdFields.add(room);
        cmdFields.add(fieldLabel("Action")); cmdFields.add(actionCombo);
        cmdCard.add(cmdTitle, BorderLayout.NORTH); cmdCard.add(cmdFields, BorderLayout.CENTER); cmdCard.add(sendBtn, BorderLayout.SOUTH);
        controls.add(cmdCard);

        // Card 3: Get status + close
        JTextField statId = styledField("AC_LIVING_ROOM_01");
        JButton getStatBtn = styledButton("Get Appliance Status", ACCENT_BLUE);
        getStatBtn.addActionListener(e -> {
            try {
                ApplianceStatusResponse res = applianceBlockingStub.withDeadlineAfter(5, TimeUnit.SECONDS)
                        .getApplianceStatus(ApplianceStatusRequest.newBuilder().setApplianceId(statId.getText().trim()).build());
                log(applianceLogArea, "GetApplianceStatus ->");
                log(applianceLogArea, "  Name   : " + res.getApplianceName());
                log(applianceLogArea, "  Status : " + res.getStatus());
                log(applianceLogArea, "  Power  : " + res.getPowerConsumption() + "W");
                log(applianceLogArea, "  Updated: " + res.getLastUpdated());
            } catch (StatusRuntimeException ex) { log(applianceLogArea, "Error: " + ex.getStatus().getDescription()); }
        });
        JButton closeBtn = styledButton("Close Stream", ACCENT_RED);
        closeBtn.addActionListener(e -> {
            if (applianceCommandObserver != null) { applianceCommandObserver.onCompleted(); applianceCommandObserver = null; log(applianceLogArea, "Control stream closed by client"); }
            else { log(applianceLogArea, "No stream is currently open"); }
        });
        JPanel statCard = new JPanel(new BorderLayout(0, 8));
        statCard.setBackground(BG_CARD);
        statCard.setBorder(BorderFactory.createCompoundBorder(new LineBorder(ACCENT_BLUE, 1, true), new EmptyBorder(12, 12, 12, 12)));
        JLabel statTitle = new JLabel("Get Status / Close Stream"); statTitle.setFont(new Font("Arial", Font.BOLD, 13)); statTitle.setForeground(ACCENT_BLUE);
        JPanel statFields = new JPanel(new GridLayout(2, 1, 0, 6)); statFields.setBackground(BG_CARD);
        statFields.add(fieldLabel("Appliance ID")); statFields.add(statId);
        JPanel statBtns = new JPanel(new GridLayout(2, 1, 0, 6)); statBtns.setBackground(BG_CARD);
        statBtns.add(getStatBtn); statBtns.add(closeBtn);
        statCard.add(statTitle, BorderLayout.NORTH); statCard.add(statFields, BorderLayout.CENTER); statCard.add(statBtns, BorderLayout.SOUTH);
        controls.add(statCard);

        tab.add(controls, BorderLayout.CENTER);
        applianceLogArea = buildLogArea();
        tab.add(wrapLog(applianceLogArea), BorderLayout.SOUTH);
        return tab;
    }

    private JPanel buildCard(String title, String[] labels, JComponent[] fields, JButton btn, Color accent) {
        JPanel card = new JPanel(new BorderLayout(0, 8));
        card.setBackground(BG_CARD);
        card.setBorder(BorderFactory.createCompoundBorder(new LineBorder(accent, 1, true), new EmptyBorder(12, 12, 12, 12)));
        JLabel titleLabel = new JLabel(title); titleLabel.setFont(new Font("Arial", Font.BOLD, 13)); titleLabel.setForeground(accent);
        card.add(titleLabel, BorderLayout.NORTH);
        if (labels.length > 0) {
            JPanel fp = new JPanel(new GridLayout(labels.length * 2, 1, 0, 6)); fp.setBackground(BG_CARD);
            for (int i = 0; i < labels.length; i++) { fp.add(fieldLabel(labels[i])); fp.add(fields[i]); }
            card.add(fp, BorderLayout.CENTER);
        }
        card.add(btn, BorderLayout.SOUTH);
        return card;
    }

    private JTextField styledField(String placeholder) {
        JTextField f = new JTextField(placeholder);
        f.setBackground(BG_INPUT); f.setForeground(TEXT_PRIMARY); f.setCaretColor(TEXT_PRIMARY);
        f.setFont(new Font("Arial", Font.PLAIN, 13));
        f.setBorder(BorderFactory.createCompoundBorder(new LineBorder(new Color(70, 70, 70), 1), new EmptyBorder(4, 8, 4, 8)));
        return f;
    }

    private JButton styledButton(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setBackground(color); btn.setForeground(Color.WHITE); btn.setFont(new Font("Arial", Font.BOLD, 13));
        btn.setFocusPainted(false); btn.setBorderPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setBorder(new EmptyBorder(8, 12, 8, 12));
        return btn;
    }

    private JLabel fieldLabel(String text) {
        JLabel l = new JLabel(text); l.setFont(new Font("Arial", Font.PLAIN, 11)); l.setForeground(TEXT_MUTED); return l;
    }

    private JLabel statusLabel(String text) {
        JLabel l = new JLabel("? " + text); l.setFont(new Font("Arial", Font.BOLD, 12)); l.setForeground(ACCENT_YELLOW); return l;
    }

    private JPanel wrapStatus(JLabel label) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT)); p.setBackground(BG_DARK); p.add(label); return p;
    }

    private JTextArea buildLogArea() {
        JTextArea area = new JTextArea(8, 0);
        area.setEditable(false); area.setBackground(new Color(10, 10, 10)); area.setForeground(ACCENT_GREEN);
        area.setFont(new Font("Monospaced", Font.PLAIN, 12)); area.setBorder(new EmptyBorder(8, 10, 8, 10));
        return area;
    }

    private JScrollPane wrapLog(JTextArea area) {
        JScrollPane scroll = new JScrollPane(area);
        scroll.setBorder(BorderFactory.createTitledBorder(new LineBorder(new Color(60, 60, 60)), " Console Log ",
                TitledBorder.LEFT, TitledBorder.TOP, new Font("Arial", Font.PLAIN, 11), TEXT_MUTED));
        scroll.setPreferredSize(new Dimension(0, 200));
        return scroll;
    }

    private void log(JTextArea area, String message) {
        SwingUtilities.invokeLater(() -> { area.append(message + "\n"); area.setCaretPosition(area.getDocument().getLength()); });
    }

    private void updateStatus(JLabel label, String text, Color color) {
        SwingUtilities.invokeLater(() -> { label.setText("? " + text); label.setForeground(color); });
    }

    private void shutdown() {
        try {
            if (energyChannel    != null) energyChannel.shutdown().awaitTermination(3, TimeUnit.SECONDS);
            if (solarChannel     != null) solarChannel.shutdown().awaitTermination(3, TimeUnit.SECONDS);
            if (applianceChannel != null) applianceChannel.shutdown().awaitTermination(3, TimeUnit.SECONDS);
        } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception ignored) {}
            SmartHomeGUI gui = new SmartHomeGUI();
            gui.setVisible(true);
            gui.addWindowListener(new WindowAdapter() {
                @Override public void windowClosing(WindowEvent e) { gui.shutdown(); }
            });
        });
    }
}
