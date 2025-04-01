package com.github.chenkai2.gixcommitx.settings;

import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.util.NlsContexts;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;

/**
 * 提交信息生成器的设置界面
 */
public class CommitMessageGeneratorSettingsConfigurable implements Configurable {
    private JPanel mainPanel;
    private JTextField urlField;
    private JTextField modelField;
    private JTextArea promptArea;
    private JTextArea systemArea;
    private JTextField temperatureField;
    private JTextField topPField;
    private JTextField apiKeyField;
    private JComboBox<String> protocolComboBox;
    private JTextField maxTokensField;

    private final CommitMessageGeneratorSettings settings = CommitMessageGeneratorSettings.getInstance();

    @Override
    public @NlsContexts.ConfigurableName String getDisplayName() {
        return "Git Commit Message Generator";
    }

    @Override
    public @Nullable JComponent createComponent() {
        mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(5, 5, 5, 5);

        // URL
        addLabelAndComponent("LLM API URL:", urlField = new JTextField(), c, 0);

        // Model
        addLabelAndComponent("LLM Model:", modelField = new JTextField(), c, 1);

        // Protocol
        protocolComboBox = new JComboBox<>(new String[]{"openai", "ollama"});
        addLabelAndComponent("API Protocol:", protocolComboBox, c, 2);

        // API Key
        addLabelAndComponent("API Key:", apiKeyField = new JTextField(), c, 3);

        // Temperature
        addLabelAndComponent("Temperature (0-1):", temperatureField = new JTextField(), c, 4);

        // Top P
        addLabelAndComponent("Top P (0-1):", topPField = new JTextField(), c, 5);

        // Max Tokens
        addLabelAndComponent("Max Tokens:", maxTokensField = new JTextField(), c, 6);

        // Prompt
        c.gridx = 0;
        c.gridy = 7;
        c.gridwidth = 1;
        mainPanel.add(new JLabel("Prompt Template:"), c);

        c.gridx = 0;
        c.gridy = 8;
        c.gridwidth = 2;
        c.weighty = 1.0;
        c.fill = GridBagConstraints.BOTH;
        promptArea = new JTextArea(5, 40);
        promptArea.setLineWrap(true);
        promptArea.setWrapStyleWord(true);
        JScrollPane promptScrollPane = new JScrollPane(promptArea);
        mainPanel.add(promptScrollPane, c);

        // System
        c.gridx = 0;
        c.gridy = 9;
        c.gridwidth = 1;
        c.weighty = 0.0;
        c.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(new JLabel("System Instruction:"), c);

        c.gridx = 0;
        c.gridy = 10;
        c.gridwidth = 2;
        c.weighty = 1.0;
        c.fill = GridBagConstraints.BOTH;
        systemArea = new JTextArea(5, 40);
        systemArea.setLineWrap(true);
        systemArea.setWrapStyleWord(true);
        JScrollPane systemScrollPane = new JScrollPane(systemArea);
        mainPanel.add(systemScrollPane, c);

        loadSettings();
        return mainPanel;
    }

    private void addLabelAndComponent(String labelText, JComponent component, GridBagConstraints c, int row) {
        c.gridx = 0;
        c.gridy = row;
        c.gridwidth = 1;
        c.weightx = 0.0;
        mainPanel.add(new JLabel(labelText), c);

        c.gridx = 1;
        c.weightx = 1.0;
        mainPanel.add(component, c);
    }

    private void loadSettings() {
        urlField.setText(settings.getLlmUrl());
        modelField.setText(settings.getLlmModel());
        promptArea.setText(settings.getLlmPrompt());
        systemArea.setText(settings.getLlmSystem());
        temperatureField.setText(String.valueOf(settings.getLlmTemperature()));
        topPField.setText(String.valueOf(settings.getLlmTopP()));
        apiKeyField.setText(settings.getLlmApiKey());
        protocolComboBox.setSelectedItem(settings.getLlmProtocol());
        maxTokensField.setText(String.valueOf(settings.getLlmMaxTokens()));
    }

    @Override
    public boolean isModified() {
        return !urlField.getText().equals(settings.getLlmUrl())
                || !modelField.getText().equals(settings.getLlmModel())
                || !promptArea.getText().equals(settings.getLlmPrompt())
                || !systemArea.getText().equals(settings.getLlmSystem())
                || !temperatureField.getText().equals(String.valueOf(settings.getLlmTemperature()))
                || !topPField.getText().equals(String.valueOf(settings.getLlmTopP()))
                || !apiKeyField.getText().equals(settings.getLlmApiKey())
                || !protocolComboBox.getSelectedItem().equals(settings.getLlmProtocol())
                || !maxTokensField.getText().equals(String.valueOf(settings.getLlmMaxTokens()));
    }

    @Override
    public void apply() throws ConfigurationException {
        settings.setLlmUrl(urlField.getText());
        settings.setLlmModel(modelField.getText());
        settings.setLlmPrompt(promptArea.getText());
        settings.setLlmSystem(systemArea.getText());
        
        try {
            settings.setLlmTemperature(Double.parseDouble(temperatureField.getText()));
            settings.setLlmTopP(Double.parseDouble(topPField.getText()));
            settings.setLlmMaxTokens(Integer.parseInt(maxTokensField.getText()));
        } catch (NumberFormatException e) {
            throw new ConfigurationException("请输入有效的数字");
        }
        
        settings.setLlmApiKey(apiKeyField.getText());
        settings.setLlmProtocol((String) protocolComboBox.getSelectedItem());
    }

    @Override
    public void reset() {
        loadSettings();
    }
}