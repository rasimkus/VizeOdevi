import java.io.*;
import java.util.Scanner;
import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;

public class Main {
    public static void main(String[] args) throws MessagingException {
        //eğer önceden bu kodla kullanicilar.txt dosyasını oluşturmadıysak dosyayı oluşturur
        DosyaKontrol nesnem1 = new DosyaKontrol();

        //ekrana ilk çıkan seçenekler menüsünü açar
        AnaMenu nesnem0 = new AnaMenu();
    }
}
class AnaMenu {
    //ekrana ilk çıkan seçenekler menüsünü açar
    //girilen seçeneğe göre uygun nesneyi oluşturur
    AnaMenu() throws MessagingException {
        Scanner sc = new Scanner(System.in);
        System.out.println("1- Elit üye ekleme");
        System.out.println("2- Genel üye ekleme");
        System.out.println("3- Mail gönderme");
        int secenek = sc.nextInt();

        switch (secenek){
            case 1:
                UyeEkleme nesnem1 = new UyeEkleme(true);//elitlik doğruysa girilen kişi elit üyeler başlığı altına yazar
                break;
            case 2:
                UyeEkleme nesnem2 = new UyeEkleme(false);//elitlik yanlışsa girilen kişi genel üyeler başlığı altına yazar
                break;
            case 3:
                MailGondermeMenu nesnem3 = new MailGondermeMenu();//2.menüyü açar
                break;
            default:
                System.out.println("hatalı giriş yaptınız");
        }
    }
}
class DosyaKontrol{
    DosyaKontrol(){
        try {
            File file = new File("kullanicilar.txt");
            //dosyanın olup olmadığına bakar. yoksa KayitliUyelerle dosyayı oluşturur
            if (!file.exists()) {
                KayitliUyeler cf = new KayitliUyeler();
            }
            else {
                BufferedReader br = new BufferedReader(new FileReader(file));
                String firstLine = br.readLine();
                br.close();
                //eğer bizim oluşturmadığımız bir kullanicilar.txt varsa o dosyayı kullanmak yerine
                //kendi dosyamızı için ilk satırı kontrol ediyoruz
                if (!firstLine.equals("° \uD801\uDC2A\uD801\uDC42 ♡ \uD801\uDC2A\uD801\uDC42 ₒ \uD801\uDC2A\uD801\uDC42 ♡ \uD801\uDC2A\uD801\uDC42 °")) {
                    //eğer ilk var olan kullanicilar.txt'nin ilk satırı bizim dosyamızdan farklıysa KayitliUyelerle dosyayı oluşturuyoruz
                    //bu sayede birden fazla kişi ekleyebiliyoruz
                    KayitliUyeler cf = new KayitliUyeler();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
class KayitliUyeler{
    //kullanicilar.txt dosyası oluşturup hali hazırda kayıtlı olan kişilerin bilgilerini bu dosyaya yazdırıyor
    KayitliUyeler(){
        try {
            //elit üyeler dizisi oluşturur
            ElitUyeler kayitliElitKullanicilar[] = new ElitUyeler[5];
            //kayıtlı olan elitüyeleri ekledik
            kayitliElitKullanicilar[0] = new ElitUyeler("Serpil", "üstebay", "serpilçdeneme@deneme.com");
            kayitliElitKullanicilar[1] = new ElitUyeler("eda", "gök", "ela@email.com");
            kayitliElitKullanicilar[2] = new ElitUyeler("ali", "gökten", "ali@email.com");

            GenelUyeler kayitliGenelKullanicilar[] = new GenelUyeler[1];
            kayitliGenelKullanicilar[0] = new GenelUyeler("ayse", "ak", "ayse@email.com");

            FileWriter output = new FileWriter("kullanicilar.txt");

            //Dosyanın, programın her run edişinde yeniden yazılmaması için diğer olası kullanıcı.txtlerden farklı olacak bir satır ekliyorum
            output.write("° \uD801\uDC2A\uD801\uDC42 ♡ \uD801\uDC2A\uD801\uDC42 ₒ \uD801\uDC2A\uD801\uDC42 ♡ \uD801\uDC2A\uD801\uDC42 °\n");
            // Elit üyeleri dosyaya yazar
            output.write("#ELİT ÜYELER#\n");
            for(int i=0;i<3;i++){
                output.write(kayitliElitKullanicilar[i].isim+"\t"+kayitliElitKullanicilar[i].soyisim+"\t"+kayitliElitKullanicilar[i].email+"\n");
            }
            // Genel üyeleri dosyaya yazar ( 1 genel üye olduğu için for kullanmadım)
            output.write("\n#GENEL ÜYELER#\n");
            output.write("Ayşe\tAk\tayse@email.com\n");

            output.close();
        }
        catch (Exception e) {
            e.getStackTrace();
        }
    }
}
class Uyeler{
    //kalıtım bir is a ilişkisi olduğu için uyeler->genel uyeler ve uyeler->elit uyeler için kalıtım kullandım
    String isim, soyisim, email;
}
class ElitUyeler extends Uyeler{
    ElitUyeler(String ad, String soyad, String eposta){
        this.isim = ad;
        this.soyisim = soyad;
        this.email = eposta;
    }
}
class GenelUyeler extends Uyeler{
    GenelUyeler(String ad, String soyad, String eposta){
        this.isim = ad;
        this.soyisim = soyad;
        this.email = eposta;
    }
}
class UyeEkleme {
    //ana menüde üye ekleme seçeneği seçildiğinde UyeEkleme classından nesne oluşturur ve constructor çalışır
    //aldığı elitlik parametresi girilen bilgilerin elit üyeler başlığı altına mı genel üyeler başlığı altına mı yazılacağını belirler
    UyeEkleme(boolean elitlik) throws MessagingException {

        try {
            //Dosyayı okuyup contents nesnesine kaydediyoruz
            StringBuilder contents = new StringBuilder();
            BufferedReader reader = new BufferedReader(new FileReader("kullanicilar.txt"));
            String line;
            while ((line = reader.readLine()) != null) {
                contents.append(line).append("\n");
            }
            reader.close();

            //kullanıcıdan eklenecek üyenin bilgilerini alıyoruz
            Scanner sc = new Scanner(System.in);
            System.out.println("eklenecek üyenin ismini giriniz");
            String isim = sc.nextLine();
            System.out.println("eklenecek üyenin soyismini giriniz");
            String soyisim = sc.nextLine();
            System.out.println("eklenecek üyenin emailini giriniz");
            String email = sc.nextLine();

            //eklenen üyenin elit olup olmadığına göre inputun dosyanın neresine yazılacağını belirliyoruz
            int index;
            if (elitlik) {
                index = contents.indexOf("#GENEL ÜYELER#") - 1;
                //mail gönderilmek istenirse diye emaili elit üyelere mail atan classa gönderiyoruz
                ElitUyelereMail nesnem = new ElitUyelereMail();
                nesnem.email= nesnem.email+","+email;
            }
            else {
                index = contents.indexOf("#GENEL ÜYELER#") + 15;
                //mail gönderilmek istenirse diye emaili genel üyelere mail atan classa gönderiyoruz
                GenelUyelereMail nesnem = new GenelUyelereMail();
                nesnem.email= nesnem.email+","+email;
            }

            //belirlediğimiz noktaya inputu giriyoruz
            contents.insert(index, "\n"+isim+"\t"+soyisim+ "\t"+email+"\n");

            BufferedWriter writer = new BufferedWriter(new FileWriter("kullanicilar.txt"));
            writer.write(contents.toString());
            writer.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("kişi sisteme eklendi");
    }
}
class MailGondermeMenu{
    //ana sayfadan mail gönderme seçeneğini seçince çıkacak menüyü gösterir
    MailGondermeMenu() throws MessagingException {
        Scanner sc = new Scanner(System.in);
        System.out.println("1- Elit üyelere mail");
        System.out.println("2- Genel üyelere mail");
        System.out.println("3- Tüm üyelere mail");
        int secenek = sc.nextInt();
        mailGonderme(secenek);
    }
    //seçeneğe göre gerekli nesneyi oluşturur ve constructorü çalıştırır
    //UyeEkleme'de mail gönderme class adresleri parametresiz constuctor ile yaptık burada ise bir int parametresi ile constructor overloading kullanıyoruz
    public void mailGonderme(int secenek) throws MessagingException {
        switch (secenek){
            case 1:
                ElitUyelereMail nesnem = new ElitUyelereMail(1);
                break;
            case 2:
                GenelUyelereMail nesnem1 = new GenelUyelereMail(1);
                break;
            case 3:
                HerkeseMailGonderme nesnem2 = new HerkeseMailGonderme(1);
                break;
            default:
                System.out.println("hatalı giriş yaptınız");
        }
    }
}
class ElitUyelereMail{
    //kayitli kullanıcıların maillerini kaydediyoruz
    //UyeEkleme'de de yeni eklenen kullanıcıların maillerini bu email stringine ekliyoruz
    String email="serpil@deneme.edu.tr,eda@email.com,ali@email.com";
    ElitUyelereMail(){}//overloading kullanabilmek için boş parametresiz constructor
    ElitUyelereMail(int a) throws MessagingException {
        //kullanıcıdan mail adresini şifresini ve yollayacağı maili alır
        Scanner sc = new Scanner(System.in);
        System.out.println("email adresinizi giriniz");
        String gonderenEmail = sc.nextLine();
        System.out.println("uygulama sifrenizi giriniz");
        String gonderenSifre = sc.nextLine();
        System.out.println("mailinizin konusunu giriniz");
        String subject = sc.nextLine();
        System.out.println("mailinizin içeriğini giriniz");
        String body = sc.nextLine();

        //mail server properties kurulumu
        Properties properties = new Properties();
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.setProperty("mail.smtp.ssl.protocols", "TLSv1.2");

        //authenticatorle yeni bir session oluşturma
        Authenticator auth = new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(gonderenEmail, gonderenSifre);
            }
        };
        Session session = Session.getInstance(properties, auth);

        //maili oluşturur
        Message msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress(gonderenEmail));
        msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email, false));
        msg.setSubject(subject);
        msg.setText(body);

        //maili yollar
        Transport.send(msg);
        System.out.println("Mailiniz başarıyla yollanmıştır");
    }
}
class GenelUyelereMail{
    //kayitli kullanıcıların maillerini kaydediyoruz
    //UyeEkleme'de de yeni eklenen kullanıcıların maillerini bu email stringine ekliyoruz
    String email="ayse@email.com";
    GenelUyelereMail(){}//overloading kullanabilmek için boş parametresiz constructor
    GenelUyelereMail(int a) throws MessagingException {
        //kullanıcıdan mail adresini şifresini ve yollayacağı maili alır
        Scanner sc = new Scanner(System.in);
        System.out.println("email adresinizi giriniz");
        String gonderenEmail = sc.nextLine();
        System.out.println("uygulama sifrenizi giriniz");
        String gonderenSifre = sc.nextLine();
        System.out.println("mailinizin konusunu giriniz");
        String subject = sc.nextLine();
        System.out.println("mailinizin içeriğini giriniz");
        String body = sc.nextLine();

        //mail server properties kurulumu
        Properties properties = new Properties();
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.setProperty("mail.smtp.ssl.protocols", "TLSv1.2");

        //authenticatorle yeni bir session oluşturma
        Authenticator auth = new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(gonderenEmail, gonderenSifre);
            }
        };
        Session session = Session.getInstance(properties, auth);

        //maili oluşturur
        Message msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress(gonderenEmail));
        msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email, false));
        msg.setSubject(subject);
        msg.setText(body);

        //maili yollar
        Transport.send(msg);
        System.out.println("Mailiniz başarıyla yollanmıştır");
    }
}
class HerkeseMailGonderme{
    HerkeseMailGonderme(){}//overloading kullanabilmek için boş parametresiz constructor
    HerkeseMailGonderme(int a) throws MessagingException {
        //ElitUyelerMail ve GenelUyelereMail classlarında oluşturduğumuz email stringlerini birleştirip bu classtaki email stringine atar
        ElitUyelereMail nesnem1 = new ElitUyelereMail();
        GenelUyelereMail nesnem2 = new GenelUyelereMail();
        String email = nesnem1.email+","+nesnem2.email;

        //kullanıcıdan mail adresini şifresini ve yollayacağı maili alır
        Scanner sc = new Scanner(System.in);
        System.out.println("email adresinizi giriniz");
        String gonderenEmail = sc.nextLine();
        System.out.println("uygulama sifrenizi giriniz");
        String gonderenSifre = sc.nextLine();
        System.out.println("mailinizin konusunu giriniz");
        String subject = sc.nextLine();
        System.out.println("mailinizin içeriğini giriniz");
        String body = sc.nextLine();

        //mail server properties kurulumu
        Properties properties = new Properties();
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.setProperty("mail.smtp.ssl.protocols", "TLSv1.2");

        //authenticator ile yeni bir session oluşturma
        Authenticator auth = new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(gonderenEmail, gonderenSifre);
            }
        };
        Session session = Session.getInstance(properties, auth);

        //maili oluşturur
        Message msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress(gonderenEmail));
        msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email, false));
        msg.setSubject(subject);
        msg.setText(body);

        //maili yollar
        Transport.send(msg);
        System.out.println("Mailiniz başarıyla yollanmıştır");
    }
}
