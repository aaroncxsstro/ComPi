# Ruta de la aplicación y el icono
$appPath = "C:\Program Files (x86)\ComPi\compi.exe"
$iconPath = "C:\Program Files (x86)\ComPi\icono.ico"
$safeIconPath = "C:\ProgramData\ComPi\icono.ico"

# Crear la carpeta destino si no existe
if (-not (Test-Path "C:\ProgramData\ComPi")) {
    New-Item -Path "C:\ProgramData\ComPi" -ItemType Directory -Force
}

# Copiar el icono a la ubicación segura
Copy-Item -Path $iconPath -Destination $safeIconPath -Force

# Ruta del registro para la extensión .copi
$extRegPath = "HKCU:\Software\Classes\.copi"
$appRegPath = "HKCU:\Software\Classes\ComPi.copi"
$iconRegPath = "HKCU:\Software\Classes\ComPi.copi\DefaultIcon"
$shellRegPath = "HKCU:\Software\Classes\ComPi.copi\shell"
$openWithCompiRegPath = "$shellRegPath\Abrir con ComPi"
$extractHereRegPath = "$shellRegPath\Extraer aquí"
$commandRegPath = "$openWithCompiRegPath\command"

# Registrar la extensión .copi
if (-not (Test-Path $extRegPath)) {
    New-Item -Path $extRegPath -Force
}
Set-ItemProperty -Path $extRegPath -Name "(default)" -Value "ComPi.copi"

# Registrar la clase de archivo
if (-not (Test-Path $appRegPath)) {
    New-Item -Path $appRegPath -Force
}
Set-ItemProperty -Path $appRegPath -Name "(default)" -Value "Archivo COPI"

# Asignar el icono personalizado desde la ubicación segura
if (-not (Test-Path $iconRegPath)) {
    New-Item -Path $iconRegPath -Force
}
Set-ItemProperty -Path $iconRegPath -Name "(default)" -Value $safeIconPath

# Añadir opción de menú contextual "Abrir con ComPi"
if (-not (Test-Path $openWithCompiRegPath)) {
    New-Item -Path $openWithCompiRegPath -Force
}
Set-ItemProperty -Path "$openWithCompiRegPath" -Name "(default)" -Value "Abrir con ComPi"

# Asignar el icono al lado de la opción "Abrir con ComPi"
$iconData = "$safeIconPath,0"
New-ItemProperty -Path $openWithCompiRegPath -Name "Icon" -Value $iconData -PropertyType String -Force

# Asignar el comando para ejecutar la aplicación con la ruta del archivo como parámetro
if (-not (Test-Path $commandRegPath)) {
    New-Item -Path $commandRegPath -Force
}
Set-ItemProperty -Path $commandRegPath -Name "(default)" -Value "`"$appPath`" `"%1`""

# Añadir opción de menú contextual "Extraer aquí"
if (-not (Test-Path $extractHereRegPath)) {
    New-Item -Path $extractHereRegPath -Force
}
Set-ItemProperty -Path "$extractHereRegPath" -Name "(default)" -Value "Extraer aqui"

# Asignar el icono al lado de la opción "Extraer aquí"
New-ItemProperty -Path $extractHereRegPath -Name "Icon" -Value $iconData -PropertyType String -Force

# Asignar el comando para ejecutar la aplicación con el archivo .copi y "extraer" como parámetros
$extractCommand = "`"$appPath`" `"%1`" extract"
if (-not (Test-Path "$extractHereRegPath\command")) {
    New-Item -Path "$extractHereRegPath\command" -Force
}
Set-ItemProperty -Path "$extractHereRegPath\command" -Name "(default)" -Value $extractCommand

# Reiniciar el Explorador de Windows para aplicar los cambios
Stop-Process -Name explorer -Force
Start-Process explorer

Write-Host "La extensión .copi ha sido registrada con el icono personalizado, opciones de menú contextual 'Abrir con ComPi' y 'Extraer aquí', y los iconos asociados."

