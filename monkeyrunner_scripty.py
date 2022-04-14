from com.android.monkeyrunner import MonkeyRunner, MonkeyDevice
from com.android.monkeyrunner.easy import EasyMonkeyDevice,By  
import time
import subprocess

def sh(command):
    p = subprocess.Popen(command, shell=True, stdout=subprocess.PIPE, stderr= subprocess.STDOUT)
    print p.stdout.read()

# Connects to the current device, returning a MonkeyDevice object
device = MonkeyRunner.waitForConnection()
easy_device = EasyMonkeyDevice(device)  

# Installs the Android package. Notice that this method returns a boolean, so you can test
# to see if the installation worked.
# device.installPackage('myproject/bin/MyApplication.apk')

# sets a variable with the package's internal name
package = 'com.asus.gamecenter'
# sets a variable with the name of an Activity in the package
activity = 'com.asus.gamecenter.GameCenterActivity'
# sets the name of the component to start
runComponent = package + '/' + activity
# Runs the component
device.startActivity(component=runComponent)
MonkeyRunner.sleep(5)

# Takes a screenshot
result = device.takeSnapshot()

# Writes the screenshot to a file
result.writeToFile('myproject/shot1.png','png')

print('rotation landscape')
device.shell('content insert --uri content://settings/system --bind name:s:accelerometer_rotation --bind value:i:0')

device.shell('content insert --uri content://settings/system --bind name:s:user_rotation --bind value:i:1')
MonkeyRunner.sleep(5)

result = device.takeSnapshot()
# Writes the screenshot to a file
result.writeToFile('myproject/shot2.png','png')
MonkeyRunner.sleep(5)

print('rotation port')
device.shell('content insert --uri content://settings/system --bind name:s:user_rotation --bind value:i:0')
MonkeyRunner.sleep(5)

easy_device.touch(By.id("id/appbar_view_type_icon"),easy_device.DOWN_AND_UP)
MonkeyRunner.sleep(5)

device.touch( 381, 2400, MonkeyDevice.DOWN_AND_UP)
MonkeyRunner.sleep(5)

device.touch( 381, 2400, MonkeyDevice.DOWN_AND_UP)
MonkeyRunner.sleep(5)


device.touch(820, 1594, MonkeyDevice.DOWN)                                           


for i in range(1, 21):                                                              
    device.touch(820, 1594 - 20*i, MonkeyDevice.MOVE)                                              
    time.sleep(0.1)                                                                            

# Remove finger from screen
device.touch(820, 1194, MonkeyDevice.UP) 

MonkeyRunner.sleep(5)
meminfo = device.shell('dumpsys meminfo com.asus.gamecenter')
print(meminfo)

MonkeyRunner.sleep(5)

device.press('KEYCODE_HOME', MonkeyDevice.DOWN_AND_UP )
