package com.artigile.howismyphonedoing.api;

import com.artigile.howismyphonedoing.api.model.*;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * Date: 6/29/13
 * Time: 9:26 AM
 *
 * @author ioanbsu
 */

public class MessageParserTest {

    @Test
    public void testUserDeviceInfoConverter() {
        MessageParserImpl messageParser = new MessageParserImpl();
        IUserDeviceModel iUserDeviceModel = new UserDeviceModel();
        iUserDeviceModel.setHumanReadableName("sdfsdf");
        IDeviceSettingsModel iDeviceSettingsModel = new DeviceSettingsModel();
        iDeviceSettingsModel.setRingerMode(RingerMode.RINGER_MODE_SILENT);
        iUserDeviceModel.setiDeviceSettingsModel(iDeviceSettingsModel);
        String serializedObject = messageParser.serialize(iUserDeviceModel);

        IUserDeviceModel iUserDeviceModelDeserialised = messageParser.parse(MessageType.DEVICE_DETAILS_INFO, serializedObject);

        assertEquals(iUserDeviceModel.getHumanReadableName(), iUserDeviceModelDeserialised.getHumanReadableName());
        assertEquals(iUserDeviceModel.getiDeviceSettingsModel().getRingerMode(), iUserDeviceModelDeserialised.getiDeviceSettingsModel().getRingerMode());


    }

    @Test
    public void testIDeviceRegistrationModel() {
        MessageParserImpl messageParser = new MessageParserImpl();
        IDeviceRegistrationModel deviceRegistrationModel = new DeviceRegistrationModel();
        deviceRegistrationModel.setUserEmail("asdasd@asdf.com");
        IDeviceModel iDeviceModel = new DeviceModel();
        iDeviceModel.setDisplay("sdfsdfsd");
        deviceRegistrationModel.setDeviceModel(iDeviceModel);

        String serializedObject = messageParser.serialize(deviceRegistrationModel);
        IDeviceRegistrationModel deviceRegistrationModelDeserialized = messageParser.parse(MessageType.REGISTER_DEVICE, serializedObject);

        assertEquals(deviceRegistrationModel.getUserEmail(), deviceRegistrationModelDeserialized.getUserEmail());
        assertEquals(deviceRegistrationModel.getDeviceModel().getDisplay(), deviceRegistrationModelDeserialized.getDeviceModel().getDisplay());
    }
}
