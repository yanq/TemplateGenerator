package yan.tools

import groovy.text.SimpleTemplateEngine

import java.util.regex.Matcher

/**
 * Created by yan on 2017/2/25.
 */
class Generator {
    static final TEMPLATE_DIR = 'template'
    private static SimpleTemplateEngine engine = new SimpleTemplateEngine()
    private static GroovyClassLoader loader = new GroovyClassLoader()

    static generateTemplate(File source,File baseDir){
        Class aClass = loader.parseClass(source)
        File parentFile = source.parentFile.parentFile
        parentFile.eachFileRecurse {
            if (it.absolutePath == source.absolutePath) return
            def relationPath = it.absolutePath.replace(parentFile.absolutePath,'')
            def className = aClass.name
            def i = className.lastIndexOf('.')
            def packageName = className.substring(0,i),classShortName = className.substring(i+1),propertyName=classShortName[0].toLowerCase()+classShortName[1..-1]
            if (relationPath.contains(classShortName)) relationPath = relationPath.replaceAll(classShortName,Matcher.quoteReplacement('${name}'))
            if (it.directory){
                new File(baseDir,"$TEMPLATE_DIR/${relationPath}").mkdirs()
            }else {
                File targetFile = new File(baseDir,"$TEMPLATE_DIR/${relationPath}")
                if (!targetFile.parentFile.exists()) targetFile.parentFile.mkdirs()
                if (targetFile.exists()){
                    println "File exist,do nothing"
                }else {
                    targetFile << it.text.replaceAll(classShortName,Matcher.quoteReplacement('${name}')).replaceAll(propertyName,Matcher.quoteReplacement('${propertyName}'))
                }
            }
        }
    }

    static generateCode(File source,File baseDir){
        Class aClass = loader.parseClass(source)
        File parentFile = new File(baseDir,TEMPLATE_DIR)
        parentFile.eachFileRecurse {
            def relationPath = it.absolutePath.replace(parentFile.absolutePath,'')
            def className = aClass.name
            def i = className.lastIndexOf('.')
            def packageName = className.substring(0,i),classShortName = className.substring(i+1),propertyName=classShortName[0].toLowerCase()+classShortName[1..-1]
            if (relationPath.contains('${name}')) relationPath = relationPath.replaceAll('\\$\\{name\\}',classShortName)
            def binding = [theClass:aClass,name:classShortName,propertyName:propertyName]
            if (it.directory){
                new File(source.parentFile.parentFile,relationPath).mkdirs()
            }else {
                File targetFile = new File(source.parentFile.parentFile,relationPath)
                if (targetFile.exists()){
                    println "File exist，do nothing"
                }else {
                    targetFile << engine.createTemplate(it).make(binding)
                }
            }
        }
    }

    static boolean isTargetFile(File file){
        return file.name.endsWith(".java") || file.name.endsWith(".groovy")
    }
}
