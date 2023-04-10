import java.io.*;
import java.util.Scanner;
import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

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

class KayitliUyeler{
    //kullanicilar.txt dosyası oluşturup hali hazırda kayıtlı olan kişilerin bilgilerini bu dosyaya yazdırıyor
    KayitliUyeler(){
        try {
            //elit üyeler dizisi oluşturur
            ElitUyeler kayitliElitKullanicilar[] = new ElitUyeler[5];
            //kayıtlı olan elitüyeleri ekledik
            kayitliElitKullanicilar[0] = new ElitUyeler("Serpil", "üstebay", "serpil@deneme.edu.tr");
            kayitliElitKullanicilar[1] = new ElitUyeler("eda", "gök", "eda@email.com");
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
            }
            else {
                index = contents.indexOf("#GENEL ÜYELER#") + 15;
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
        System.out.println("1- Tüm üyelere mail :( elit ve genel diye ayıramadım");
        int secenek = sc.nextInt();
        mailGonderme(secenek);
    }
    //seçeneğe göre gerekli nesneyi oluşturur ve constructorü çalıştırır
    //UyeEkleme'de mail gönderme class adresleri parametresiz constuctor ile yaptık burada ise bir int parametresi ile constructor overloading kullanıyoruz
    public void mailGonderme(int secenek) throws MessagingException {
        switch (secenek){
            case 1:
                SendMail nesnem=new SendMail();
                nesnem.MailSender();
                break;
            default:
                System.out.println("hatalı giriş yaptınız");
        }
    }
}
class DosyadanMailBulma {
    public ArrayList<String> Email() { // dosyadaki mail adreslerini bulup ArrayList olarak return eden method

        ArrayList<String> emails = new ArrayList<String>();
        String fileName ="kullanicilar.txt" ;

        try {
            // dosyayı okuyup mail adreslerini belirliyor ve bu mail adreslerini arrayliste kadediyoruz
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            String line;
            while ((line = reader.readLine()) != null) {
                Pattern pattern = Pattern.compile("\\b[\\w.%-]+@[-.\\w]+\\.[A-Za-z]{2,4}\\b");
                Matcher matcher = pattern.matcher(line);
                while (matcher.find()) {
                    String email = matcher.group();
                    emails.add(email);
                }
            }
            reader.close();
        } catch (Exception e) { // hata ile karşılaşılırsa:
            System.err.format("'%s' okunurken hata oluştu.", fileName);
            e.printStackTrace();
        }
        // dosyadan okuduğumuz mail adreslerin kontrol ediyoruz
        System.out.println(emails.size() + " tane adres bulundu");
        for (String email : emails) {
            System.out.println(email);

        }
        return emails; }
}
class SendMail extends DosyadanMailBulma {

    public void MailSender(){
        // kalıtım yoluyla aldığımız arraylist:
        ArrayList<String> emails = Email();
        Scanner scn = new Scanner(System.in);
        
        Scanner sc= new Scanner(System.in);
        System.out.println("email adresinizi giriniz:");
        String username=sc.nextLine();
        System.out.println("iki adımlı doğrulama şifrenizi giriniz");
        String password=sc.nextLine();
        

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("ilaydatiryaki25@gmail.com"));

            for (String address : emails) { // kalıtım ile aldığımız arrayListteki mail adreslerini tek tek atıyoruz
                message.addRecipients(Message.RecipientType.TO, InternetAddress.parse(address));
            }

            // mailin içerik bilgisini alıyoruz
            System.out.println("mailinizin konusunu giriniz:");
            String subject = scn.nextLine();
            message.setSubject(subject);
            System.out.println("mailinizin içeriğini giriniz:");
            String body = scn.nextLine();
            message.setText(body);


            // maili gönderiyoruz
            Transport.send(message);

            System.out.println("Mail gönderme başarılı!");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}

