# Add project specific ProGuard rules here.
# NiñoBiólogo no habilita minificación en la build de referencia (ver build.gradle.kts),
# pero se documentan reglas básicas por si se activa en el futuro.
-keep class com.educalab.ninobiologo.data.local.entity.** { *; }
-keep class com.educalab.ninobiologo.domain.model.** { *; }
-dontwarn kotlinx.coroutines.**
