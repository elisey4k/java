import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public class MainForm {

    private JPanel rootPanel; // корневая панель, создаётся автоматически

    private DefaultTableModel tableModel;
    private JTextField textFieldN;
    private JTextField textFieldTime;
    private JTextField textFieldResult;
    private JTable table;
    private JButton btnClear;
    private JButton btnDelete;
    private JButton btnAdd;

    public MainForm() {
        // 1. Настройка модели таблицы (3 колонки)
        String[] columns = {"Число N", "Время (сек)", "Получившееся число"};
        tableModel = new DefaultTableModel(columns, 0);
        table.setModel(tableModel);
        // Разрешаем редактирование ячеек (базовый CRUD)
        table.setFillsViewportHeight(true);

        // 2. Назначаем слушателей кнопкам
        btnAdd.addActionListener(e -> addRecord());
        btnDelete.addActionListener(e -> deleteRecord());
        btnClear.addActionListener(e -> clearInputFields());

        // 3. Горячие клавиши (Key Bindings)
        setupKeyboardShortcuts();

        // 4. Дополнительно: приятный внешний вид (Nimbus Look&Feel)
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
            SwingUtilities.updateComponentTreeUI(rootPanel);
        } catch (Exception ignored) {}
    }

    private void addRecord() {
        String nStr = textFieldN.getText().trim();
        String timeStr = textFieldTime.getText().trim();
        String resultStr = textFieldResult.getText().trim();

        // Проверка на пустое время и отрицательное значение
        if (timeStr.isEmpty()) {
            JOptionPane.showMessageDialog(rootPanel, "Введите время!", "Ошибка", JOptionPane.ERROR_MESSAGE);
            textFieldTime.requestFocus();
            return;
        }
        try {
            double time = Double.parseDouble(timeStr);
            if (time < 0) {
                JOptionPane.showMessageDialog(rootPanel, "Время не может быть отрицательным!", "Ошибка", JOptionPane.ERROR_MESSAGE);
                textFieldTime.requestFocus();
                return;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(rootPanel, "Время должно быть числом!", "Ошибка", JOptionPane.ERROR_MESSAGE);
            textFieldTime.requestFocus();
            return;
        }

        // Дополнительно: можно проверять, что N и Result не пустые (но по заданию не обязательно)
        if (nStr.isEmpty() || resultStr.isEmpty()) {
            JOptionPane.showMessageDialog(rootPanel, "Заполните все поля!", "Ошибка", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Добавляем строку в таблицу
        Object[] row = {nStr, timeStr, resultStr};
        tableModel.addRow(row);
        clearInputFields(); // после добавления очищаем поля
    }

    private void deleteRecord() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            tableModel.removeRow(selectedRow);
        } else {
            JOptionPane.showMessageDialog(rootPanel, "Выберите строку для удаления", "Предупреждение", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void clearInputFields() {
        textFieldN.setText("");
        textFieldTime.setText("");
        textFieldResult.setText("");
        textFieldN.requestFocus();
    }

    private void setupKeyboardShortcuts() {
        // Получаем InputMap и ActionMap для корневой панели (JPanel)
        InputMap inputMap = rootPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = rootPanel.getActionMap();

        // Backspace -> очистить поля
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_BACK_SPACE, 0), "clear");
        actionMap.put("clear", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearInputFields();
            }
        });

        // Delete -> удалить строку
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0), "delete");
        actionMap.put("delete", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteRecord();
            }
        });

        // Ctrl+N -> добавить запись
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_N, KeyEvent.CTRL_DOWN_MASK), "add");
        actionMap.put("add", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addRecord();
            }
        });
    }

    public JPanel getRootPanel() {
        return rootPanel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("CRUD - Результаты производительности");
            MainForm mainForm = new MainForm();
            frame.setContentPane(mainForm.getRootPanel());
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.pack();
            frame.setMinimumSize(new Dimension(600, 400));
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }

   
}