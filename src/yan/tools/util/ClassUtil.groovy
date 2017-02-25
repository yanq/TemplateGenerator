package yan.tools.util

import java.util.regex.Matcher

/**
 * Created by yan on 2017/2/25.
 */
class ClassUtil {

    static String shortName(Class aClass){
        aClass.name.substring(aClass.name.lastIndexOf('.')+1)
    }

    static String propertyName(Class aClass){
        shortName(aClass)[0].toLowerCase()+shortName(aClass)[1..-1]
    }

    static String packageName(Class aClass){
        aClass.name.substring(0,aClass.name.lastIndexOf('.'))
    }

    static String packagePath(Class aClass){
        packageName(aClass).split('\\.').join(File.separator)
    }

    static String classPath(Class aClass){
        aClass.name.split('\\.').join(File.separator)
    }

    public static void main(String[] args) {
        Class aClass1 = String.class
        println shortName(aClass1)
        println propertyName(aClass1)
        println packageName(aClass1)
        println packagePath(aClass1)
        println classPath(aClass1)
        println new File('.').canonicalPath

        File source = new File("D:\\IdeaProjects\\TemplateGenerator\\src\\yan\\tools\\GenerateTemplate.java")
        def parentPath = source.canonicalPath.substring(0,source.canonicalPath.lastIndexOf("yan\\tools\\GenerateTemplate"))
        println source.canonicalPath
        println source.absolutePath
        println parentPath
        println source.canonicalPath.replaceAll(Matcher.quoteReplacement(parentPath),'')
    }
}
