# Derleme

Gerekenler: JDK 17, Python 3, Android SDK platform 35 ve Android build tools 35. Proje Gradle veya harici Android çalışma zamanı kitaplığı gerektirmez.

`SADIE_BUILD_SDK` altında `jdk-*`, `android-15` (aapt2, d8, zipalign, apksigner) ve `android-35/android.jar` beklenir. Yerel SDK düzenine göre `tools/build.sh` yollarını düzenleyebilirsin.

```bash
export SADIE_BUILD_SDK=/path/to/sdk-bundle
export SADIE_SIGNING_KEY=/private/path/release.jks
read -s SADIE_KEY_PASSWORD
export SADIE_KEY_PASSWORD
bash tools/build.sh
```

Mevcut bir imzalama anahtarı gerekir. Yeni bir geliştirici kendi anahtarını üretip kullanabilir; farklı anahtarla imzalanan APK mevcut kurulumun üzerine güncellenemez. Yayımlanan APK'nın paket adı `com.sadienotes.app`.

## Arayüz kontrolü

Node.js ve Playwright kullanılır. Python sunucusunu ayrı terminalde çalıştır:

```bash
npm install playwright
npx playwright install chromium
python3 -m http.server 8771 --bind 127.0.0.1 --directory app/src/main/assets
node tools/test-ui.cjs
```

Gerekirse `NODE_PATH` ve `CHROMIUM_PATH` ortam değişkenleriyle mevcut kurulumlarını seç. Test ekran görüntüleri ve rapor `outputs/` altına yazılır. Testlerin örnek notları yalnızca geçici tarayıcı profillerinde üretilir, gerçek APK'ya eklenmez.

## Yedek şifreleme kontrolü

JDK ve JSON-java gerekir (org.json 20260814 ile test edildi; resmi Maven Central paketinden edinilebilir). Android Base64 için host test uyarlaması `tools/crypto-test/` içinde; uygulamaya dahil edilmez. Üretim `Crypto.java` dosyası test edilir, Android Keystore yolu test kapsamı dışındadır.

```bash
export ANDROID_JAR=/path/to/android-35/android.jar
export JSON_JAR=/path/to/json-20260814.jar
bash tools/test-crypto.sh
```

## Gerçek cihazda kontrol listesi

- Not oluştur/düzenle, uygulamayı kapat/aç, Unicode ve uzun metinleri kontrol et.
- PIN ekle/değiştir/kaldır; arka plana geçişte kilidi ve beş yanlış denemeyi kontrol et.
- Bildirim iznini reddet/kabul et; gelecekte hatırlatıcı planla, yeniden başlat ve bildirimin doğru notu açtığını kontrol et.
- Görsel/PDF/ses ekle; dış uygulamada aç. Ekleri dahil eden parolalı yedeği başka kurulumda geri yükle.
- Yanlış parola/bozuk yedek durumunda mevcut verinin korunduğunu kontrol et.
- PDF yazdırmayı, dosya kaydetme iptalini, depolama doluluğunu ve ekran/klavye kenar boşluklarını kontrol et.
