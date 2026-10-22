# SadieNotes

Düşüncelerine, planlarına ve küçük anlarına sakin bir yer. Android 8+ için Türkçe, çevrimdışı not ve görev uygulaması.

**[SadieNotes.apk indir](https://github.com/nightmare608/SadieNotes/releases/download/v1.0.0/SadieNotes.apk)** · **[Son sürüm](https://github.com/nightmare608/SadieNotes/releases/latest)**

![SadieNotes](https://github.com/nightmare608/SadieNotes/releases/download/v1.0.0/01-desktop.png)

## Yaz ve düzenle

- Zengin metin: kalın, italik, altı/üstü çizili, üç başlık düzeyi, sıralı/maddeli listeler, alıntı, kod bloğu, bağlantı, tablo ve ayırıcı.
- Markdown düzenleme ve önizleme; kod blokları, başlıklar ve listeler. Düzenleyiciler arasında geçiş yapılabilir; zengin metinden Markdown'a geçiş biçimi sadeleştirir ve önceki metni geçmişe alır.
- Otomatik kayıt, kelime/karakter sayısı ve tahmini okuma süresi.
- Son 10 düzenleme oturumunun başlangıç metnine dönüş. Görevler ve ekler sürüm geçmişi kapsamında değildir.
- Toplantı, günlük, ders, proje, okuma, fikir, haftalık plan ve alışveriş için sekiz şablon.

## Düzenle ve bul

- Klasörler, etiketler, favoriler, sabitleme, arşiv ve geri yüklenebilir çöp kutusu.
- Çoklu seçim, toplu klasöre taşıma, arşivleme, silme, geri yükleme ve kalıcı silme.
- Türkçe büyük/küçük harf uyumlu arama: başlık, içerik, etiket, görev ve ek adı.
- Son düzenleme, oluşturma, başlık veya hatırlatıcı tarihine göre sıralama; kart/liste görünümü.
- Açık/karanlık/sistem teması, yazı boyutu, yazı tipi, yazım denetimi, varsayılan klasör ve haftalık hedef.

## Planla ve hatırla

- Nota bağlı görevler, tamamlanma, bitiş tarihi ve öncelik.
- Tüm notlardan görevleri bir araya getiren görev görünümü.
- Aylık takvimde görev tarihleri ve not hatırlatıcıları.
- Android bildirimleri; yeniden başlatmadan sonra gelecekteki hatırlatıcılar tekrar planlanır.
- Bildirim içeriği genel tutulur; notun metni bildirimde gösterilmez.

## Ekler ve aktarım

- Dosya, görsel, PDF veya mevcut ses dosyası ekleme. Görsel küçük önizlemeleri; ekleri ilgili uygulamada açma.
- Metin, Markdown, HTML ve not JSON dışa aktarımı; metin/Markdown ve not JSON içe aktarımı.
- Android paylaşım menüsü ve sistem yazdırma ekranından PDF kaydetme.
- Notlar, klasörler, görevler, etiketler, geçmiş, ayarlar ve ekleri içeren tam yedek.
- İsteğe bağlı parola ile şifreli yedek; parolasız JSON yedek de desteklenir.
- Geri yükleme, açık onaydan sonra mevcut verinin yerini alır. Önce yedek al.

## Gizlilik ve güvenlik

Android APK internet izni istemez; sunucu, reklam, analitik veya hesap içermez. Notlar ve ekler Android Keystore'daki uygulamaya ait AES anahtarıyla AES-256-GCM kullanılarak şifreli saklanır. Not kaydı AtomicFile ile yazılır. Bu, kilidi açılmış/ele geçirilmiş bir cihaza karşı mutlak güvenlik garantisi değildir.

İsteğe bağlı uygulama PIN'i en az 6 rakamdır. PIN özeti rastgele tuz ve PBKDF2-HMAC-SHA256 (210.000 tur) ile saklanır; art arda beş yanlış denemeden sonra 30 saniye bekleme uygulanır. Uygulama arka plana geçince kilitlenir; uygulamanın açtığı dosya seçiciden normal dönüş bu akışı kesmez. PIN, cihazdaki şifreleme anahtarının kendisi değildir.

Parolalı yedekler, cihaz anahtarından bağımsız PBKDF2-HMAC-SHA256 (210.000 tur, 256 bit), rastgele 16 bayt tuz ve 12 bayt IV ile AES-GCM kullanır. Böylece doğru parola ile başka kurulumda geri yüklenebilir. PIN veya yedek parolası için kurtarma hizmeti yoktur. Parolasız yedekler okunabilir veri içerir.

Cihazdaki şifreli veriler, uygulama kaldırılırsa veya cihaz anahtarı kaybolursa kurtarılamaz. Dosya sistemi şifrelemesi bulut yedeğinin yerini tutmaz. Düzenli dış yedek al.

## Sınırlar ve mevcut olmayan özellikler

- Bulut eşitleme, ortak düzenleme, el yazısı/OCR ve doğrudan ses kaydı bu sürümde yoktur. Mevcut ses dosyaları eklenebilir.
- Hatırlatıcılar kesin zamanlı alarm değildir; Android bildirim izni, pil yönetimi ve sistem koşulları etkiler. Uygulamayı zorla durdurmak hatırlatıcıları uygulama tekrar açılana kadar engelleyebilir.
- Not verisi toplam 12 MB; içe aktarılan not gövdesi en fazla 2 milyon karakter; ek başına 20 MB; tam yedekte eklerin toplamı 40 MB. Yedek dosyası okuma sınırı 120 MB.
- Çöp kutusu otomatik boşaltılmaz. Kalıcı silme geri alınamaz. Paylaşılan ek yalnızca hiçbir not tarafından kullanılmıyorsa temizlenir.
- Sadece metin/HTML/Markdown/not JSON dışa aktarımında eklerin dosya içeriği dahil değildir; onları tam yedekle taşı.
- Tarayıcı önizlemesi localStorage kullanır ve şifreli değildir. Android PIN, Keystore, dosya ekleri, parolalı yedek, sistem hatırlatıcıları ve PDF yazdırma akışı APK içinde sunulur.

## Kurulum ve doğrulama

APK'yı Android 8+ cihazına indirip aç. Gerekirse dosyayı açtığın uygulama için APK yükleme izni ver. Uygulama ilk açılışta sistem izni istemez; bildirim izni ilk hatırlatıcıda sorulur.

176 tarayıcı arayüzü/kalıcılık/güvenlik/köprü kontrolü 360, 390, 768 ve 1280 piksel genişliklerde geçti. Üretimdeki Crypto.java üzerinde 13 host JVM kontrolü parolalı yedek dönüşünü, yanlış parolayı, değiştirilmiş şifreli veriyi ve geçersiz parametreleri sınar. APK v2/v3 imzaları ve kaynak/paket yapısı doğrulandı. Bu kontroller gerçek Android cihaz testinin yerini tutmaz; bağlı cihaz bulunmadığından Keystore, bildirim, PIN yaşam döngüsü, dosya sağlayıcı ve yazdırma gerçek cihazda henüz doğrulanmadı.

## Geliştirme

`BUILDING.md` derleme ve test adımlarını açıklar. Kaynak depoda düzenlenebilir biçimde bulunur; sürüme ayrıca kaynak ZIP'i ve doğrulama raporu eklenir. İmzalama anahtarı/parolası kaynakta yer almaz.
