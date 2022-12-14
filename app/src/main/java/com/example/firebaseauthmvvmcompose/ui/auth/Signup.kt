package com.example.firebaseauthmvvmcompose.ui.auth

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TextField
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.firebaseauthmvvmcompose.R
import com.example.firebaseauthmvvmcompose.data.Resource
import com.example.firebaseauthmvvmcompose.navaigation.ROUTE_HOME
import com.example.firebaseauthmvvmcompose.navaigation.ROUTE_LOGIN
import com.example.firebaseauthmvvmcompose.navaigation.ROUTE_SIGNUP
import com.example.firebaseauthmvvmcompose.ui.theme.FirebaseAuthMvvmComposeTheme
import com.example.firebaseauthmvvmcompose.ui.theme.spacing

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignupScreen(viewModel: AuthViewModel?, navController: NavController) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val signupFlow = viewModel?.signupFlow?.collectAsState()


    ConstraintLayout(modifier = Modifier.fillMaxSize()) {
        val spacing = MaterialTheme.spacing
        val (refHeader, refName, refEmail, refPassword, refButton, refTextHasAccount, refLoading) = createRefs()

        Box(modifier = Modifier
            .constrainAs(refHeader) {
                top.linkTo(parent.top, spacing.extraLarge)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }
            .wrapContentSize()) {
            AuthHeader()
        }

        TextField(
            value = name,
            onValueChange = {
                name = it
            },
            label = { Text(text = stringResource(id = R.string.name)) },
            modifier = Modifier.constrainAs(refName) {
                top.linkTo(refHeader.bottom, spacing.extraLarge)
                start.linkTo(parent.start, spacing.large)
                end.linkTo(parent.end, spacing.large)
                width = Dimension.fillToConstraints
            },
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.None,
                autoCorrect = false,
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            )
        )

        /*email*/
        TextField(
            value = email,
            onValueChange = {
                email = it
            },
            label = { Text(text = stringResource(id = R.string.email)) },
            modifier = Modifier.constrainAs(refEmail) {
                top.linkTo(refName.bottom, spacing.medium)
                start.linkTo(parent.start, spacing.large)
                end.linkTo(parent.end, spacing.large)
                width = Dimension.fillToConstraints
            },
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.None,
                autoCorrect = false,
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            )
        )

        /*password*/
        TextField(
            value = password,
            onValueChange = {
                password = it
            },
            label = { Text(text = stringResource(id = R.string.password)) },
            modifier = Modifier.constrainAs(refPassword) {
                top.linkTo(refEmail.bottom, spacing.medium)
                start.linkTo(parent.start, spacing.large)
                end.linkTo(parent.end, spacing.large)
                width = Dimension.fillToConstraints
            },
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.None,
                autoCorrect = false,
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            )
        )

        /*button*/
        Button(onClick = {
            viewModel?.signup(name, email, password)
        },
            modifier = Modifier.constrainAs(refButton) {
                top.linkTo(refPassword.bottom, spacing.large)
                start.linkTo(parent.start, spacing.extraLarge)
                end.linkTo(parent.end, spacing.extraLarge)
                width = Dimension.fillToConstraints
            }) {
            Text(
                text = stringResource(id = R.string.signup),
                color = colorResource(id = R.color.white)
            )
        }

        /*text signup*/
        Text(
            text = stringResource(id = R.string.already_have_account),
            style = androidx.compose.material3.MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = androidx.compose.material3.MaterialTheme.colorScheme.onSurface,
            modifier = Modifier
                .constrainAs(refTextHasAccount) {
                    top.linkTo(refButton.bottom, spacing.medium)
                    start.linkTo(parent.start, spacing.extraLarge)
                    end.linkTo(parent.end, spacing.extraLarge)
                }
                .clickable {
                    navController.navigate(ROUTE_LOGIN) {
                        popUpTo(ROUTE_SIGNUP) { inclusive = true }
                    }
                }
        )

        //observer part
        signupFlow?.value?.let {
            when (it) {
                is Resource.Failure -> {
                    val context = LocalContext.current
                    Toast.makeText(context, it.exception.message, Toast.LENGTH_SHORT).show()
                }
                Resource.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.constrainAs(refLoading) {
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    })
                }
                is Resource.Success -> {
                    LaunchedEffect(Unit) {
                        navController.navigate(ROUTE_HOME) {
                            popUpTo(ROUTE_HOME) { inclusive = true }
                        }
                    }
                }
            }
        }

    }
}

@Preview(showBackground = true)
@Composable
fun SignupScreenPreview() {
    FirebaseAuthMvvmComposeTheme {
        SignupScreen(null, navController = rememberNavController())
    }
}