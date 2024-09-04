import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.questconnect.R

data class NavbarButton(
    val leadingIcon: ImageVector,
    val color: Color,
    val onClick: () -> Unit,
    val centerButton: Boolean
)

@Composable
fun Navbar() {
    val buttons = listOf(
        NavbarButton(
            leadingIcon = ImageVector.vectorResource(id = R.drawable.icons8_steam),
            color = MaterialTheme.colorScheme.primary,
            onClick = { /* Handle click */ },
            centerButton = true
        ),
        NavbarButton(
            leadingIcon = Icons.Default.Home,
            color = Color.Gray,
            onClick = { /* Handle click */ },
            centerButton = false
        ),
        NavbarButton(
            leadingIcon = Icons.Default.CheckCircle,
            color = Color.Gray,
            onClick = { /* Handle click */ },
            centerButton = false
        ),
    )

    Box(modifier = Modifier.fillMaxSize()) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter),
            color = MaterialTheme.colorScheme.secondary
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                buttons.filterNot { it.centerButton }.take(1).forEach { button ->
                    bottomButtonIcon(
                        leadingIcon = button.leadingIcon,
                        color = button.color,
                        onClick = button.onClick,
                        centerButton = button.centerButton
                    )
                }

                Spacer(modifier = Modifier.weight(1f)) // Space before center button

                buttons.find { it.centerButton }?.let { button ->
                    bottomButtonIcon(
                        leadingIcon = button.leadingIcon,
                        color = button.color,
                        onClick = button.onClick,
                        centerButton = button.centerButton
                    )
                }

                Spacer(modifier = Modifier.weight(1f)) // Space after center button

                // Add right side buttons
                buttons.filterNot { it.centerButton }.drop(1).forEach { button ->
                    bottomButtonIcon(
                        leadingIcon = button.leadingIcon,
                        color = button.color,
                        onClick = button.onClick,
                        centerButton = button.centerButton
                    )
                }
            }
        }
    }
}

@Composable
fun bottomButtonIcon(
    leadingIcon: ImageVector,
    color: Color,
    onClick: () -> Unit,
    centerButton: Boolean
) {
    val buttonSize = if (centerButton) 72.dp else 56.dp // Increase size if center button

    Icon(
        imageVector = leadingIcon,
        //tint = color,
        contentDescription = "Icon",
        modifier = Modifier
            .size(buttonSize)
            .clickable {
                onClick()
            }
            .padding(10.dp)
            .background(color = color, shape = RoundedCornerShape(8.dp))
    )

}

@Preview
@Composable
fun PreviewNavbar() {
    Navbar()
}
