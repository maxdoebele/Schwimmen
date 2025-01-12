package View.gui

object Style {
  val buttonStyle = """
    -fx-background-color: #007178;          /* Button color */
    -fx-text-fill: white;                    /* Text color */
    -fx-border-radius: 8px;                  /* Rounded corners */
    -fx-border-width: 0;                     /* No border */
    -fx-font-family: 'Inter', sans-serif;     /* Font */
    -fx-font-size: 16px;                     /* Font size */
    -fx-font-weight: bold;                   /* Bold text */
    -fx-padding: 13px 20px;                  /* Padding */
    -fx-cursor: hand;                        /* Pointer cursor */
    -fx-alignment: center;                   /* Center text */
    -fx-background-radius: 8px;              /* Rounded background */
    -fx-effect: dropshadow(gaussian, #007178, 3, 0, 0, 3); /* Shadow effect */
"""
  val disabledButton = """
    -fx-background-color: #c9cfce;          /* Button color */
    -fx-text-fill: white;                    /* Text color */
    -fx-border-radius: 8px;                  /* Rounded corners */
    -fx-border-width: 0;                     /* No border */
    -fx-font-family: 'Inter', sans-serif;     /* Font */
    -fx-font-size: 16px;                     /* Font size */
    -fx-font-weight: bold;                   /* Bold text */
    -fx-padding: 13px 20px;                  /* Padding */
    -fx-cursor: hand;                        /* Pointer cursor */
    -fx-alignment: center;                   /* Center text */
    -fx-background-radius: 8px;              /* Rounded background */
    -fx-effect: dropshadow(gaussian, #c9cfce, 3, 0, 0, 3); /* Shadow effect */
"""

  val defaultText = """
      -fx-font-size: 14px;
      -fx-fill: black;
"""
  val defaultTextWhite = """
      -fx-font-size: 14px;
      -fx-fill: white;
"""
  val defaultTextRed = """
      -fx-font-size: 14px;
      -fx-fill: red;
"""
  val boldText = """
      -fx-font-size: 15px;
      -fx-font-weight: bold;
      -fx-text-fill: black;
"""
  val boldTextWhite = """
      -fx-font-size: 15px;
      -fx-font-weight: bold;
      -fx-text-fill: white;
"""
  val BorderBlack = """
      -fx-background-color: transparent;
      -fx-border-color: black;
      -fx-border-width: 2px;
      -fx-border-radius: 10px;
"""
  val Background = """
      -fx-background-image: url('file:src/main/resources/Background.png');
      -fx-background-size: cover;
      -fx-background-position: center;
"""
}