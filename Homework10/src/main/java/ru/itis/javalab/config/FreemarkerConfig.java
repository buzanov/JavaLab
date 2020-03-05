package ru.itis.javalab.config;


import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

import javax.servlet.ServletContext;
import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.util.Map;

public class FreemarkerConfig {

    private static Configuration configuration;

    private FreemarkerConfig() {
    }

    public static Configuration getInstance(String realPath) {
        if (configuration == null) {
            configuration = new Configuration(Configuration.VERSION_2_3_29);
            configurateConfig(configuration, realPath);
        }
        return configuration;
    }

    private static void configurateConfig(Configuration configuration, String pathToTemplates) {
        File file = new File(pathToTemplates);
        try {
            configuration.setDirectoryForTemplateLoading(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        configuration.setDefaultEncoding("UTF-8");
        configuration.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        configuration.setLogTemplateExceptions(false);
        configuration.setWrapUncheckedExceptions(true);
        configuration.setFallbackOnNullLoopVariable(false);
    }

    public static void preprocessConfig(Writer out, Map<String, Object> root, ServletContext context, String templateName) {
        Template template = null;
        try {
            template = FreemarkerConfig.getInstance(context.getRealPath("/WEB-INF/templates")).getTemplate(templateName);
            template.process(root, out);
        } catch (TemplateException | IOException e) {
            e.printStackTrace();
        }
    }
}
