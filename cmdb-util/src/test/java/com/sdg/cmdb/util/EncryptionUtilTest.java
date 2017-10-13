package com.sdg.cmdb.util;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by zxxiao on 2016/11/8.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:springtest/context.xml"})
public class EncryptionUtilTest {

    @Test
    public void testencrypt() {
        String pwd = EncryptionUtil.encrypt("-----BEGIN RSA PRIVATE KEY-----\n" +
                "FUCKogIBAAKCAQEAzuswWPArXG6aShTC3ax63+pYOgYYe51YeICqBzOmcAv8MXWD\n" +
                "wIQR/xlpzLQVlW198RKiOnvAt/8YWIJ261XnxhGM/zNpKM0ZWBRCNqsAC6XKJbPC\n" +
                "sY9SQASaKAmhB9m8IQzQvKOd0MxVmZ8FGDQdggYEtXqaUI3ypcc79A0bY53CJJI3\n" +
                "FEZYZ8uVwxDy1B4fLQAzBadxqxmVBtGpP+GRHKzc6RkUoiNvBWY+mVUEdBe+auRR\n" +
                "/gZ3OdSbuoXMJPTfnjqEvMU/QEpzMF1+2Abu+X+y/l+0zJ+b2exCff5f1Fx5s0NT\n" +
                "GleqBt0SLul0+Naha5fcdnpQWBrCe26fkKycYwIBIwKCAQEAq3Jp40qnlbq6WqN8\n" +
                "447pd7OK7j+QoPAWGrPAFJiCl1pjPuxXORz49/B8O+Wre8/AH4R3yg7EQK7LB4IK\n" +
                "wv4JLxXbOdoj7ptPg36AAWkdS3q9b7Izmm9wCShiePIJFSIfiRk/PTccXIwMaVff\n" +
                "plb5pnoD5tNMqSUoIvWJcnFClFym38v31yActsBhXdggetnNm/R6fsFVZ6QPwdjV\n" +
                "kcqDWzDZobSN9NV5ihzWVy1/2VcLDs1v9yBY4BdOsNbj4RIO178duLKZQu5QOv3B\n" +
                "j5tdZa3O4JPgtj0if3sAP8hci6sM5G0DN7Dw5HlxBDXkuj50sMKWVtb3jyb5EIfn\n" +
                "spusKwKBgQDujV2SeTtIM5C+UYFoKdFCcs/W6hq0t/DKpMNXCuFPD3S4AJWFLEXh\n" +
                "ciBQNmyJlG1kzQ1pPeqhRt5M51bDDjQzkYQXDRVJQc5TeRhV03L00LSRN7EAd3rn\n" +
                "gGZDfoPwJvXptu4stPkwawpvjRw6WdeXPkd9/Fb6PZkkPkZfdt7yMQKBgQDeDYUY\n" +
                "YnGMciaFQBXWCVemyQB40gCCA1EhS/b4LMU5llV2hnSvKxDgaCv0hu+GKM6KEGNh\n" +
                "ne4s5IHnBpWdyVKaIs/FZthpKDrZXmstwTC+Z3TzIZJM2k2uBknSjzBYQqDzL0RX\n" +
                "em5xW/CepYwvZ500hB7m04nTDKIHtnKSZY1e0wKBgA2hrZNXYnenhJ0pOpg852Lh\n" +
                "/T97F3gKgEnARa0z0lxKBqttv2ayEp8rF8oRvQ8tDZC7QphMuUsLXSj3RslRRM+/\n" +
                "LB6TCIfX3+eDQzgathyeNjQvEW29r0BtvLNmUK6jI/65/vs9ipUNbk+EaANVftkZ\n" +
                "gG2ZZA5MqapMs5BtMU+rAoGAEwh5H1jlKUuc5tmbeL744mkAClsknW3/o8SuxNCj\n" +
                "MNJfGMmyOuZvKS2AHEYUh9ecrL+TfWVO7eezXPHvkS6DbEwgcAGAQ4cyTSVZpNYL\n" +
                "fgjeI3fn9/V031+f7Xn81F17go8F25zHo1EF/vg/N0NlPwQCphIhwaH/Qn1aRxAE\n" +
                "zZ0CgYEAti0Fne7isUzcS3RfZn2/uMHKglwSn/FLTrkdNtElTd58U78iV8tsGKg0\n" +
                "nlZ9ppZ3ZlOU91OE6LFDgV7yQe1YwWvKUFgXvzRDi/Y6zw+9ufaW9RobYajxOkeh\n" +
                "aakZWj7qbK16/88PTP8Sk91OzIWF5TSpW834/Yua/SckAdYPYcU=\n" +
                "-----END RSA PRIVATE KEY-----\n");
        System.err.println(pwd);
    }

    @Test
    public void testencryptByToy() {
        String pwd = EncryptionUtil.encrypt("-----BEGIN RSA PRIVATE KEY-----\n" +
                "FUCKoQIBAAKCAQEAsYyt2cnUkg3CcUtgWtTlb9MWXc/ub5dTgF4HjJferQp6NHzl\n" +
                "DvTZzVM3AUi2ZCoz7KuFZ22yRvAN69/1lUs4sk/V73kxFmuN/6ZziquAptypqoEN\n" +
                "L8FypPSpcfAU1d1HMtbI/XKQcMbHkxI9jyiAFHHHJkzwYB7LYEWUcTr0iJbBgYO6\n" +
                "/b6tBnXacx1IoPLF2fPov2D4W6NlYrF4JlviEA504dDYky0YQHrOpqDEPhEVU1ON\n" +
                "c6FVXNeXbxdONyH5gcpLgR8g0nHXPlC4phhow0CAzZLxAxEFSDwppCms36NLUaCc\n" +
                "c9wcI8bsA68ELL2vvP7qMWraqAk+rCfg2qRC0QIBIwKCAQEAnUIWUy8bXMpbw26l\n" +
                "zMsxnY8Tz28GYtZ9KI3MHBF8FZuQz2dOiZcKDaF53JDcD5MYDCLj30PnC6FraoSJ\n" +
                "DzP/BFVXFe7/l4PViqUfBcsaLWRbwudU0obEoLtxgixqOcP9O6hLnqdMu6jOc6Jx\n" +
                "CcTJNq3q52i3pZeeMLKoDISss3y8f9fzBi2xEc2IpZq9+BxSlyf95ihSQuAOLqNI\n" +
                "NpJxDmDOkbtFl5bdTBLYmhcRa9Nco7IASyGdgWDRsY2Q+89bqvLcMIJSKzwWLttg\n" +
                "tJE4JFJIkYDG3Rott1Nc3FCVHqHvd7bq7p5fdICbvkXsUAPlXnLqdkpWgzTWqfct\n" +
                "uLKbiwKBgQDcQ8iU4N/ezFY3NICY7crLorEH/GJemd/DBOMTM2heZuuQ7/K3Fgk7\n" +
                "bP42tf2I6RzZh+Dqf+UaTXiuCCKnWxULvB6a471EpbNtC5uZ+xUIM7y3+fMhk/hJ\n" +
                "ZpiYzvmRhGsRu5klzRPxI538qC+4SVUDf6uLzNsOAoJbDiJmBA5fwQKBgQDOWs+X\n" +
                "3BaSsmkhvXIbSHNa53CQjZdFI63k71gbrRRe2veoJAqx4oHKM0ZIjHzM0vUV5C3L\n" +
                "6TtwdPpJCdjypvmGRrqGdyFN2uXuDlIUlNMIZoG49YpJv9ZhCmZ6kh9y/tP5q8VU\n" +
                "oaG/Oo95Ux/Vup4UoSQ0pTOchQDo/yg2s3mnEQKBgFHQCKxThloRYdn9jtm3aJTH\n" +
                "ZlNsXw0x1sTHTQchuQ0e6cgem/rcTJHqT8stp1AcEgenf20K7rH/hJhpa/UEksnY\n" +
                "KJ/uMFtTfSiAp4JV8d55nd3vH8qkrK2MgdJbgUSthtqzZMTlxZQUiyNUaX72qoxF\n" +
                "XPlpWK1upUJHFBdDVcvLAoGAEbADKkYQkDstlS1+zyN3mhPWcspOy2l1TiMkzywX\n" +
                "sFvpV44Pi5cScGrLgo+95auum3n1S/4MaLmRy78D94qnrGx2Y0wKKz6l/nZBjLxM\n" +
                "mlHt3KdU/wHQi/mTwVz7YaDQVzqb4q7EuJ6elV7leLg5b3uGwq8TDWp8a74ZY8Y9\n" +
                "oJsCgYAdQ5QQFrkXVG/DK4hcUwb8CeOu30PQBaAEyN/3qIQBGFeJ4RMd4/iCwdSv\n" +
                "HGq/61cBe/mR2v7XXf3vFD3ea+vF7sFZTh1pYsHn/mK8Go5c4utqakatY+LLcHE4\n" +
                "XSIoTLTx3Kwt1roUXwlyHp8VoxGKyznBZ72/6NQrw4nuiV459U==\n" +
                "-----END RSA PRIVATE KEY-----\n");
        System.err.println(pwd);
    }


    @Test
    public void testdecrypt() {
        String pwd = "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";
          String result = EncryptionUtil.decrypt(pwd);

        System.err.println(result);
    }
}
