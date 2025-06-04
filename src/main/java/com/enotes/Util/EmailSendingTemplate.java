package com.enotes.Util;

public class EmailSendingTemplate {

    public static String sendEmailForVerification(String username, String url) {
        return """
                <!DOCTYPE html>
                <html>
                <head>
                    <style>
                        body {
                            font-family: Arial, sans-serif;
                            line-height: 1.6;
                            color: #333;
                            max-width: 600px;
                            margin: 0 auto;
                            padding: 20px;
                        }
                        .header {
                            background-color: #4CAF50;
                            color: white;
                            padding: 10px 20px;
                            text-align: center;
                            border-radius: 5px 5px 0 0;
                        }
                        .content {
                            padding: 20px;
                            border: 1px solid #ddd;
                            border-top: none;
                            border-radius: 0 0 5px 5px;
                        }
                        .button {
                            display: inline-block;
                            padding: 10px 20px;
                            background-color: #4CAF50;
                            color: white;
                            text-decoration: none;
                            border-radius: 5px;
                            margin: 15px 0;
                        }
                        .footer {
                            margin-top: 20px;
                            font-size: 0.8em;
                            color: #777;
                            text-align: center;
                        }
                    </style>
                </head>
                <body>
                    <div class="header">
                        <h2>Email Verification</h2>
                    </div>
                    <div class="content">
                        <p>Hello %s,</p>
                        <p>Thank you for registering with eNotes. Please verify your email address to complete your registration.</p>
                
                        <a href="%s" class="button">Verify Email Address</a>
                
                        <p>If the button above doesn't work, copy and paste the following link into your browser:</p>
                        <p>%s</p>
                
                        <p>If you didn't create an account with eNotes, you can safely ignore this email.</p>
                    </div>
                    <div class="footer">
                        <p>© 2025 eNotes. All rights reserved.</p>
                    </div>
                </body>
                </html>
                """.formatted(username, url, url);
    }
}